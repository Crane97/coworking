package com.monforte.coworking.services.impl;

import com.monforte.coworking.domain.entities.Reservation;
import com.monforte.coworking.exceptions.OverlapErrorException;
import com.monforte.coworking.repositories.ReservationRepository;
import com.monforte.coworking.services.IReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ReservationService implements IReservationService {

    @Autowired
    public ReservationRepository reservationRepository;

    public List<Reservation> getReservations(){ return reservationRepository.findAll(); }

    public Reservation getReservationById(Integer id) throws NoSuchElementException {

        Optional<Reservation> res = reservationRepository.findById(id);

        if(res.isPresent()) {
            return res.get();
        }
        else{ throw new NoSuchElementException("No reservation with id "+ id);}
    }

    public Reservation addReservation(Reservation reservation) throws OverlapErrorException {
        if(compareLocalDateTimesReservations(reservation.getStart(), reservation.getEnd())){
            return reservationRepository.save(reservation);
        }
        else{
            throw new OverlapErrorException("The reservation time is overlaping with another appointment.");
        }
    }

    public Reservation updateReservation(Reservation reservation) throws OverlapErrorException{

        if(compareLocalDateTimesReservations(reservation.getStart(), reservation.getEnd())){
            return reservationRepository.save(reservation);
        }
        else{
            throw new OverlapErrorException("The reservation time is overlaping with another appointment.");
        }
    }

    public void deleteReservation(Integer id){ reservationRepository.deleteById(id); }

    public boolean compareLocalDateTimesReservations(LocalDateTime start, LocalDateTime end){

        boolean overlap = true;

        for(Reservation aux : getReservations()){
            if(start.isAfter(aux.getStart()) && start.isBefore(aux.getEnd())){
                overlap = false;
            }
            if(end.isBefore(aux.getEnd()) && end.isAfter(aux.getStart())){
                overlap = false;
            }
            if(start.isBefore(aux.getStart()) && end.isAfter(aux.getEnd())){
                overlap = false;
            }
        }

        return overlap;
    }

    public List<Reservation> getReservationsByRoom(){
        return null;
    }

}
