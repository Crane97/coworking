package com.monforte.coworking.controller;

import com.monforte.coworking.domain.dto.requests.ReservationRecursiveTO;
import com.monforte.coworking.domain.dto.requests.ReservationRequestTO;
import com.monforte.coworking.domain.entities.Reservation;
import com.monforte.coworking.exceptions.ApiErrorException;
import com.monforte.coworking.exceptions.OverlapErrorException;
import com.monforte.coworking.services.IReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping(path = "/api/reservation")
public class ReservationController {

    @Autowired
    public IReservationService reservationService;

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

    @GetMapping(path = "/room/{roomid}")
    public ResponseEntity<List<Reservation>> getReservationByRoom(@PathVariable Integer roomId){
        List<Reservation> reservation1 = reservationService.getReservationsByRoom(roomId);
        return new ResponseEntity<>(reservation1, HttpStatus.OK);
    }


    @PostMapping(path = "/reservation/recursive")
    public ResponseEntity<List<Reservation>> addRecursiveReservation(@RequestBody ReservationRecursiveTO reservationRecursiveTO) throws OverlapErrorException {
        List<Reservation> result = reservationService.addRecursiveReservations(reservationRecursiveTO);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping(path = "/room/{roomId}/day/{day}/{month}/{year}")
    public ResponseEntity<List<LocalTime>> getAvailableTimeByDay(@PathVariable Integer roomId, @PathVariable int day,@PathVariable int month,@PathVariable int year){
        LocalDate dayOfMonth = LocalDate.of(year,month,day);
        System.out.println(dayOfMonth);
        List<LocalTime> availableTimeTO = reservationService.getAvailableTimeByRoomByDay(roomId, dayOfMonth);
        return new ResponseEntity<>(availableTimeTO, HttpStatus.OK);
    }

    @PostMapping(path = "/add/normalReservation")
    public ResponseEntity<Reservation> addNormalReservation(@RequestBody ReservationRequestTO reservation) throws ApiErrorException, OverlapErrorException {
        Reservation reservation1 = reservationService.addNormalReservation(reservation);
        return new ResponseEntity<>(reservation1, HttpStatus.CREATED);
    }
}
