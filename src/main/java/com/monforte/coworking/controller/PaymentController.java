package com.monforte.coworking.controller;

import com.monforte.coworking.exceptions.InvoiceNotFoundException;
import com.monforte.coworking.http.PaymentIntentDTO;
import com.monforte.coworking.services.IPaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/api/stripe")
public class PaymentController {

    @Autowired
    public IPaymentService paymentService;

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

}
