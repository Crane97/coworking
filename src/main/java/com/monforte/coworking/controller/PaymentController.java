package com.monforte.coworking.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.monforte.coworking.domain.entities.User;
import com.monforte.coworking.exceptions.InvoiceNotFoundException;
import com.monforte.coworking.domain.dto.requests.PaymentIntentDTO;
import com.monforte.coworking.services.ICheckoutService;
import com.monforte.coworking.services.IPaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/api/stripe")
public class PaymentController {

    @Autowired
    public IPaymentService paymentService;

    @Autowired
    public ICheckoutService checkoutService;

    @PostMapping("/paymentIntent/{id}")
    public ResponseEntity<String> createPayment(@RequestBody PaymentIntentDTO paymentIntentDTO, @PathVariable("id") Integer id) throws StripeException, InvoiceNotFoundException {
        PaymentIntent paymentIntent = paymentService.createPaymentIntent(paymentIntentDTO, id);
        return new ResponseEntity<>(paymentIntent.toJson(), HttpStatus.OK);
    }

    @PostMapping("/confirm/{id}/{idInvoice}")
    public ResponseEntity<String> confirmPayment(@PathVariable("id") String id, @PathVariable("idInvoice") Integer idInvoice) throws StripeException, InvoiceNotFoundException {
        PaymentIntent paymentIntent = paymentService.confirmPaymentIntent(id, idInvoice);
        return new ResponseEntity<>(paymentIntent.toJson(), HttpStatus.OK);
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<String> cancelPayment(@PathVariable("id") String id) throws StripeException {
        PaymentIntent paymentIntent = paymentService.cancelPaymentIntent(id);
        return new ResponseEntity<>(paymentIntent.toJson(), HttpStatus.OK);
    }

    @PostMapping("/checkout/session/{priceId}/{userId}")
    public ResponseEntity<String> createCheckout(@PathVariable("priceId") String priceId, @PathVariable("userId") Integer userId) throws StripeException {
        return new ResponseEntity<>(checkoutService.createCheckout(priceId, userId), HttpStatus.OK);
    }

    @PostMapping("/portal/session")
    public ResponseEntity<String> createPortal(@RequestBody User user) throws StripeException {
        return new ResponseEntity<>(checkoutService.createPortal(user.getCustomer()), HttpStatus.OK);
    }

    @PostMapping("/refund/{id}")
    public ResponseEntity<Refund> refundPayment(@PathVariable("id") String id) throws StripeException, InvoiceNotFoundException {
        return new ResponseEntity<>(paymentService.refundReservation(id),HttpStatus.OK);
    }

    @PostMapping("/webhook")
    @ResponseStatus(value = HttpStatus.OK)
    public void handleEvent(@RequestBody Event event) throws StripeException, JsonProcessingException {
        paymentService.handleEvent(event);
    }

}
