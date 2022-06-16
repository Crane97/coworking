package com.monforte.coworking.services.impl;

import com.monforte.coworking.domain.entities.User;
import com.monforte.coworking.services.ICheckoutService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.checkout.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CheckoutService implements ICheckoutService {

    @Autowired
    public UserService userService;

    @Value("${stripe.key.private}")
    String secretKey;

    @Value("${checkout.success.key}")
    String success_url;

    @Value("${checkout.cancel.key}")
    String cancel_url;

    public String createCheckout(String priceId, Integer userId) throws StripeException {
        Stripe.apiKey = secretKey;

        List<Object> lineItems = new ArrayList<>();
        Map<String,Object> lineItem1 = new HashMap<>();

        lineItem1.put("price", priceId);
        lineItem1.put("quantity", 1);
        lineItems.add(lineItem1);
        Map<String, Object> params = new HashMap<>();

        params.put(
                "success_url",
                success_url
        );
        params.put(
                "cancel_url",
                cancel_url
        );
        params.put("line_items", lineItems);
        params.put("client_reference_id", userId);
        params.put("mode", "subscription");

        Session session = Session.create(params);

        return session.toJson();
    }

    public String createPortal(String customer) throws StripeException {
        Stripe.apiKey = secretKey;


        Map<String,Object> params = new HashMap<>();

        params.put("customer", customer);
        params.put("return_url", cancel_url);

        com.stripe.model.billingportal.Session session = com.stripe.model.billingportal.Session.create(params);

        return session.toJson();
    }
}
