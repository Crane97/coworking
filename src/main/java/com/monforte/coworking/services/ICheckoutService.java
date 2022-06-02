package com.monforte.coworking.services;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;

public interface ICheckoutService {

    String createCheckout(String priceId) throws StripeException;
}
