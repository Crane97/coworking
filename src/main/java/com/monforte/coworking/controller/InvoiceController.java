package com.monforte.coworking.controller;

import com.monforte.coworking.domain.entities.Invoice;
import com.monforte.coworking.exceptions.InvoiceNotFoundException;
import com.monforte.coworking.services.IInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/api/invoice")
public class InvoiceController {

    @Autowired
    public IInvoiceService invoiceService;

    @GetMapping(path ="/reservation/{reservationId}")
    public ResponseEntity<Invoice> getInvoiceByReservationId(@PathVariable("reservationId") Integer reservationId) throws InvoiceNotFoundException {
        Invoice invoice = invoiceService.getInvoiceByReservationId(reservationId);
        return new ResponseEntity<>(invoice, HttpStatus.OK);
    }
}
