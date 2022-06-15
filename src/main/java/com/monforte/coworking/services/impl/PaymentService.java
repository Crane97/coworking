package com.monforte.coworking.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monforte.coworking.domain.entities.Invoice;
import com.monforte.coworking.domain.entities.User;
import com.monforte.coworking.exceptions.InvoiceNotFoundException;
import com.monforte.coworking.domain.dto.requests.PaymentIntentDTO;
import com.monforte.coworking.services.IInvoiceService;
import com.monforte.coworking.services.IPaymentService;
import com.monforte.coworking.services.IUserService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventData;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Slf4j
public class PaymentService implements IPaymentService {

    @Autowired
    public IInvoiceService invoiceService;

    @Autowired
    public IUserService userService;

    @Autowired
    public ReservationService reservationService;

    @Value("${stripe.key.private}")
    String secretKey;

    public PaymentIntent createPaymentIntent(PaymentIntentDTO paymentIntentDTO, Integer id) throws StripeException, InvoiceNotFoundException {
        Stripe.apiKey = secretKey;
        Map<String, Object> params = new HashMap<>();
        params.put("description", paymentIntentDTO.getDescription());
        params.put("amount", paymentIntentDTO.getAmount());
        params.put("currency", paymentIntentDTO.getCurrency());
        params.put("payment_method", paymentIntentDTO.getPayment_method());

        ArrayList payment_method_types = new ArrayList();
        payment_method_types.add("card");
        params.put("payment_method_types", payment_method_types);

        PaymentIntent result = PaymentIntent.create(params);

        //Update del invoice y ponerle el id del payment Intent
        Invoice invoice = invoiceService.getInvoice(id);

        invoice.setStatus(result.getStatus());
        invoice.setNumber(result.getId());
        invoiceService.updateInvoice(invoice);

        return result;
    }

    public PaymentIntent confirmPaymentIntent(String id, Integer idInvoice) throws StripeException, InvoiceNotFoundException {
        Stripe.apiKey = secretKey;
        PaymentIntent paymentIntent = PaymentIntent.retrieve(id);

        Map<String, Object> params = new HashMap<>();
        params.put("payment_method", "pm_card_visa");

        PaymentIntent result = paymentIntent.confirm(params);

        Invoice invoice = invoiceService.getInvoice(idInvoice);

        invoice.setStatus(result.getStatus());
        invoiceService.updateInvoice(invoice);

        return result;
    }

    public PaymentIntent cancelPaymentIntent(String id) throws StripeException {
        Stripe.apiKey = secretKey;
        PaymentIntent paymentIntent = PaymentIntent.retrieve(id);
        paymentIntent.cancel();

        return paymentIntent;
    }

    public Refund refundReservation(String id) throws StripeException, InvoiceNotFoundException {
        Stripe.apiKey = secretKey;

        Map<String, Object> params = new HashMap<>();
        params.put("payment_intent", id);

        Integer amount = 0;

        amount = invoiceService.refundReservation(id);

        params.put("amount", amount);

        return Refund.create(params);
    }

    public void handleEvent(Event event) throws StripeException, JsonProcessingException {
        Stripe.apiKey = secretKey;

        switch (event.getType()) {
            case "checkout.session.completed":

                Event checkoutComplete = Event.retrieve(event.getId());

                EventData eventData = checkoutComplete.getData();
                Map<String, Object> checkoutCompleteMap = new ObjectMapper().readValue(eventData.toJson(), HashMap.class);


                LinkedHashMap<String, Object> linkedCheckout = (LinkedHashMap<String, Object>) checkoutCompleteMap.get("object");

                Integer userid = Integer.parseInt((String) linkedCheckout.get("client_reference_id"));

                User user = userService.getUser(userid);

                user.setCustomer((String) linkedCheckout.get("customer"));
                user.setSubscription((String) linkedCheckout.get("subscription"));
                userService.updateUser(user);

                log.info("checkout.session.completed");
                break;
            case "invoice.paid":
                // Continue to provision the subscription as payments continue to be made.
                // Store the status in your database and check when a user accesses your service.
                // This approach helps you avoid hitting rate limits.
                log.info("invoice.paid");

                Event invoicePaid = Event.retrieve(event.getId());

                EventData eventDataInvoice = invoicePaid.getData();
                Map<String, Object> invoicePaidCompleteMap = new ObjectMapper().readValue(eventDataInvoice.toJson(), HashMap.class);


                LinkedHashMap<String, Object> linkedMapInvoice = (LinkedHashMap<String, Object>) invoicePaidCompleteMap.get("object");

                String userCustomer = (String) linkedMapInvoice.get("customer");

                User user1 = userService.getUserByCustomer(userCustomer);

                user1.setSubscription((String) linkedMapInvoice.get("subscription"));
                user1.setPartner(true);
                userService.updateUser(user1);


                //Get user by customer id
                //update subscription
                //partner a TRUE

                break;
            case "invoice.payment_failed":
                // The payment failed or the customer does not have a valid payment method.
                // The subscription becomes past_due. Notify your customer and send them to the
                // customer portal to update their payment information.
                log.info("invoice.payment_failed");
                break;
            default:
                // System.out.println("Unhandled event type: " + event.getType());
        }
    }

}