package com.monforte.coworking.services;

import com.monforte.coworking.domain.entities.Invoice;
import com.monforte.coworking.domain.entities.Reservation;
import com.monforte.coworking.exceptions.InvoiceNotFoundException;

import java.util.List;

public interface IInvoiceService {

    Invoice newInvoice(List<Reservation> reservationList);

    Invoice getInvoiceByReservationId(Integer reservationId) throws InvoiceNotFoundException;
}
