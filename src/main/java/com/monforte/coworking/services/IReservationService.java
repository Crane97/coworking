package com.monforte.coworking.services;

import com.monforte.coworking.domain.entities.Reservation;
import com.monforte.coworking.domain.entities.Room;
import com.monforte.coworking.exceptions.OverlapErrorException;

import java.time.LocalDateTime;
import java.util.List;

public interface IReservationService {

    List<Reservation> getReservations();

    Reservation getReservationById(Integer id);

    Reservation addReservation(Reservation reservation) throws OverlapErrorException;

    Reservation updateReservation(Reservation reservation) throws OverlapErrorException;

    void deleteReservation(Integer id);

    List<Reservation> getReservationsByRoom(Integer roomid);

    boolean compareLocalDateTimesReservations(LocalDateTime start, LocalDateTime end);
}
