package com.monforte.coworking.services;

import com.monforte.coworking.exceptions.InvoiceNotFoundException;
import com.monforte.coworking.http.PaymentIntentDTO;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;

public interface IPaymentService {

    PaymentIntent createPaymentIntent(PaymentIntentDTO paymentIntentDTO, Integer id) throws StripeException, InvoiceNotFoundException;

    PaymentIntent confirmPaymentIntent(String id, Integer idInvoice) throws StripeException, InvoiceNotFoundException;

    PaymentIntent cancelPaymentIntent(String id) throws StripeException;

    Refund refundReservation(String id) throws StripeException, InvoiceNotFoundException;

}
