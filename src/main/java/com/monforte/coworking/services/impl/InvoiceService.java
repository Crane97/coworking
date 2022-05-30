package com.monforte.coworking.services.impl;

import com.monforte.coworking.domain.entities.Invoice;
import com.monforte.coworking.domain.entities.Reservation;
import com.monforte.coworking.domain.entities.enums.RoomType;
import com.monforte.coworking.exceptions.InvoiceNotFoundException;
import com.monforte.coworking.repositories.InvoiceRepository;
import com.monforte.coworking.services.IInvoiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class InvoiceService implements IInvoiceService {

    @Autowired
    public InvoiceRepository invoiceRepository;

    public double priceFlexible = 2;
    public double priceFixed = 3;
    public double priceReunion = 10;

    public double[] discountNoPartner = {0,0.05,0.15,0.2,0.3,0.4,0.5,0.65,0.7};
    public double[] discountPartner = {0.1,0.15,0.2,0.3,0.4,0.5,0.57,0.7,0.72};

    public Invoice getInvoiceByReservationId(Integer reservationId) throws InvoiceNotFoundException {

        Optional<Invoice> invoice = invoiceRepository.findFirstByReservationsId(reservationId);

        if(invoice.isEmpty()){
            log.error("Invoice related with Reservation id "+ reservationId + " not found in the database");
            throw new InvoiceNotFoundException("Invoice not found in the database");
        }

        return invoice.get();
    }

    @Transactional
    public Invoice newInvoice(List<Reservation> reservationList){
        Invoice invoice = new Invoice();
        invoice.setReservations(reservationList);

        double totalTime = 0;
        double discount = 0;
        double result = 0;

        //Vamos a sacar el número total de horas que se han reservado para realizar los descuentos correspondientes.
        for(Reservation res : reservationList){
            if(res != null && res.getStart()!= null && res.getEnd() != null) {
                totalTime += getTimeBetweenDates_inMinutes(res.getStart(), res.getEnd());
            }
        }

        totalTime = totalTime / 60;


        if(reservationList.get(0).getUser() != null && reservationList.get(0).getUser().getPartner()){
            //User IS PARTNER
            if(totalTime < 8) { discount = discountPartner[0]; }
            if((8 <= totalTime) && (totalTime < 20)) { discount = discountPartner[1]; }
            if((20 <= totalTime) && (totalTime < 35)) { discount = discountPartner[2]; }
            if((35 <= totalTime) && (totalTime < 50)) { discount = discountPartner[3]; }
            if((50 <= totalTime) && (totalTime < 80)) { discount = discountPartner[4]; }
            if((80 <= totalTime) && (totalTime < 100)) { discount = discountPartner[5]; }
            if((100 <= totalTime) && (totalTime < 150)) { discount = discountPartner[6]; }
            if((150 <= totalTime) && (totalTime < 200)) { discount = discountPartner[7]; }
            if(totalTime > 200) { discount = discountPartner[8]; }
        }
        else {
            //User IS NOT PARTNER
            if(totalTime < 8) { discount = discountNoPartner[0]; }
            if((8 <= totalTime) && (totalTime < 20)) { discount = discountNoPartner[1]; }
            if((20 <= totalTime) && (totalTime < 35)) { discount = discountNoPartner[2]; }
            if((35 <= totalTime) && (totalTime < 50)) { discount = discountNoPartner[3]; }
            if((50 <= totalTime) && (totalTime < 80)) { discount = discountNoPartner[4]; }
            if((80 <= totalTime) && (totalTime < 100)) { discount = discountNoPartner[5]; }
            if((100 <= totalTime) && (totalTime < 150)) { discount = discountNoPartner[6]; }
            if((150 <= totalTime) && (totalTime < 200)) { discount = discountNoPartner[7]; }
            if(totalTime > 200) { discount = discountNoPartner[8]; }
        }

        //Comprobamos la sala
        if(!reservationList.isEmpty()){
            if(reservationList.get(0).getRoom() != null && reservationList.get(0).getRoom().getRoomType().equals(RoomType.FLEXIBLE)){
                invoice.setTotalAmount(getPrice(priceFlexible,totalTime,discount));
            }
            if(reservationList.get(0).getRoom() != null && reservationList.get(0).getRoom().getRoomType().equals(RoomType.FIXED)){
                invoice.setTotalAmount(getPrice(priceFixed,totalTime,discount));
            }
            if(reservationList.get(0).getRoom() != null && reservationList.get(0).getRoom().getRoomType().equals(RoomType.REUNION)){
                invoice.setTotalAmount(getPrice(priceReunion,totalTime,discount));
            }
        }

        invoice.setStatus("pending");
        invoice.setIssued(LocalDateTime.now());
        invoice.setCurrency("EUR");

        invoiceRepository.save(invoice);

        return invoice;
    }

    public double getTimeBetweenDates_inMinutes(LocalDateTime time1, LocalDateTime time2){
        double result = 0;
        int hours = 0;
        int minutes = 0;

        hours = time2.getHour() - time1.getHour();
        minutes = time2.getMinute() - time1.getMinute();

        result = (double) hours * 60 + minutes;

        return result;
    }

    public double getPrice(double price, double totalTime, double discount){
        return (price-(price*discount))*totalTime;
    }
}
