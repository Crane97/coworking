package com.monforte.coworking.services;

import com.monforte.coworking.entities.Reservation;
import com.monforte.coworking.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    @Autowired
    public ReservationRepository reservationRepository;

    public List<Reservation> getReservations(){ return reservationRepository.findAll(); }

    public Reservation getReservationById(Integer id){ return  reservationRepository.findById(id).get(); }

    public Reservation addReservation(Reservation reservation){ return reservationRepository.save(reservation); }

    public Reservation updateReservation(Reservation reservation){ return reservationRepository.save(reservation); }

    public void deleteReservation(Integer id){ reservationRepository.deleteById(id); }

}
