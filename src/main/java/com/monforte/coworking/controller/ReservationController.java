package com.monforte.coworking.controller;

import com.monforte.coworking.entities.Reservation;
import com.monforte.coworking.exceptions.ApiErrorException;
import com.monforte.coworking.exceptions.OverlapErrorException;
import com.monforte.coworking.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(path = "/api/reservation")
public class ReservationController {

    @Autowired
    public ReservationService reservationService;

    @GetMapping
    public ResponseEntity<List<Reservation>> getReservations(){
        List<Reservation> reservation1 = reservationService.getReservations();
        return new ResponseEntity<>(reservation1, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Reservation> getReservation(@PathVariable Integer id){
        Reservation reservation1 = reservationService.getReservationById(id);
        return new ResponseEntity<>(reservation1, HttpStatus.OK);
    }

    @PostMapping(path = "/add")
    public ResponseEntity<Reservation> addReservation(@RequestBody Reservation reservation) throws ApiErrorException, OverlapErrorException {

        boolean isBefore = reservation.getStart().isBefore(reservation.getEnd());

        if(isBefore) {
            Reservation reservation1 = reservationService.addReservation(reservation);
            return new ResponseEntity<>(reservation1, HttpStatus.CREATED);
        }
        else{
            throw new ApiErrorException("The start date is not before the end date");
        }
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<Reservation> updateReservation(@RequestBody Reservation request, @PathVariable Integer id) throws ApiErrorException, OverlapErrorException {

        boolean isBefore = request.getStart().isBefore(request.getEnd());

        if(isBefore) {
            Reservation reservation = reservationService.getReservationById(id);

            if (request.getDescription() != null) reservation.setDescription(request.getDescription());
            if (request.getStart() != null) reservation.setStart(request.getStart());
            if (request.getEnd() != null) reservation.setEnd(request.getEnd());
            if (request.getCanceledAt() != null) reservation.setCanceledAt(request.getCanceledAt());
            if (request.getStatus() != null) reservation.setStatus(request.getStatus());

            Reservation reservation1 = reservationService.updateReservation(reservation);
            return new ResponseEntity<>(reservation1, HttpStatus.OK);
        }
        else{
            throw new ApiErrorException("The start date is not before the end date");
        }
    }

    @DeleteMapping(path = "/delete/{id}")
    public void deleteReservation(@PathVariable Integer id){
        reservationService.deleteReservation(id);
    }
}
