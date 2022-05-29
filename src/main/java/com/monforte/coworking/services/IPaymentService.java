package com.monforte.coworking.services;

import com.monforte.coworking.http.PaymentIntentDTO;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

public interface IPaymentService {

    PaymentIntent createPaymentIntent(PaymentIntentDTO paymentIntentDTO) throws StripeException;

    PaymentIntent confirmPaymentIntent(String id) throws StripeException;

    PaymentIntent cancelPaymentIntent(String id) throws StripeException;

}
