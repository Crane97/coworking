package com.monforte.coworking.services;

import com.monforte.coworking.domain.dto.requests.ReservationRecursiveTO;
import com.monforte.coworking.domain.dto.requests.ReservationRequestTO;
import com.monforte.coworking.domain.dto.responses.MyReservationsTO;
import com.monforte.coworking.domain.entities.Reservation;
import com.monforte.coworking.exceptions.InvoiceNotFoundException;
import com.monforte.coworking.exceptions.OverlapErrorException;
import com.stripe.exception.StripeException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface IReservationService {

    List<Reservation> getReservations();

    List<MyReservationsTO> getReservationsAsTO();

    Reservation getReservationById(Integer id);

    Reservation addReservation(Reservation reservation) throws OverlapErrorException;

    Reservation updateReservation(Reservation reservation) throws OverlapErrorException;

    void deleteReservation(Integer id) throws InvoiceNotFoundException, StripeException;

    List<Reservation> getReservationsByRoom(Integer roomid);

    List<Reservation> getReservationsByRoomByDay(Integer roomid, LocalDate day);

    boolean compareLocalDateTimesReservations(LocalDateTime start, LocalDateTime end, List<Reservation> reservationList);

    List<Reservation> addRecursiveReservations(ReservationRecursiveTO reservationRecursiveTO);

    List<Reservation> addDaysReservation(ReservationRecursiveTO reservationRecursiveTO);

    List<LocalTime> getAvailableTimeByRoomByDay(Integer roomid, LocalDate day);

    Reservation addNormalReservation(ReservationRequestTO reservationTO);

    List<MyReservationsTO> getReservationsByUser(Integer id);
}
