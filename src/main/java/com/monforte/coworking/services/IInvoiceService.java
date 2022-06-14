package com.monforte.coworking.services;

import com.monforte.coworking.domain.dto.responses.ReservationInvoiceTO;
import com.monforte.coworking.domain.entities.Invoice;
import com.monforte.coworking.domain.entities.Reservation;
import com.monforte.coworking.exceptions.InvoiceNotFoundException;
import com.monforte.coworking.exceptions.ReservationNotFoundException;

import java.util.List;

public interface IInvoiceService {

    Invoice newInvoice(List<Reservation> reservationList);

    Invoice getInvoiceByReservationId(Integer reservationId) throws InvoiceNotFoundException;

    ReservationInvoiceTO getReservationInvoiceTOByReservationId(Integer reservationId) throws InvoiceNotFoundException, ReservationNotFoundException;

    void updateInvoice(Invoice invoice);

    void updateInvoicePayAtDoor(Integer id) throws InvoiceNotFoundException;

    Invoice getInvoice(Integer id) throws InvoiceNotFoundException;

    Integer refundReservation(String number) throws InvoiceNotFoundException;

    void deleteInvoiceById(Integer id);
}
