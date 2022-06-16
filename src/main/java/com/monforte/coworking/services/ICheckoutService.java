package com.monforte.coworking.services;

import com.stripe.exception.StripeException;

public interface ICheckoutService {

    String createCheckout(String priceId, Integer id) throws StripeException;

    String createPortal(String customer) throws StripeException;
}
