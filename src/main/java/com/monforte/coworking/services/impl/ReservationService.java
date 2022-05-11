package com.monforte.coworking.services.impl;

import com.monforte.coworking.domain.dto.requests.ReservationRecursiveTO;
import com.monforte.coworking.domain.entities.Reservation;
import com.monforte.coworking.domain.entities.Room;
import com.monforte.coworking.exceptions.OverlapErrorException;
import com.monforte.coworking.repositories.ReservationRepository;
import com.monforte.coworking.repositories.RoomRepository;
import com.monforte.coworking.services.IReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ReservationService implements IReservationService {

    @Autowired
    public ReservationRepository reservationRepository;

    @Autowired
    public RoomRepository roomRepository;

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
            if((start.isAfter(aux.getStart()) && start.isBefore(aux.getEnd())) || start.isEqual(aux.getStart())){
                overlap = false;
            }
            if((end.isBefore(aux.getEnd()) && end.isAfter(aux.getStart())) || end.isEqual(aux.getEnd())){
                overlap = false;
            }
            if(start.isBefore(aux.getStart()) && end.isAfter(aux.getEnd())){
                overlap = false;
            }
        }

        return overlap;
    }

    public List<Reservation> getReservationsByRoom(Integer roomid){
        Room room = roomRepository.getById(roomid);
        return reservationRepository.findByRoom(room);
    }

    public List<Reservation> getReservationsByRoomByDay(Integer roomid, LocalDate day){
        Room room = roomRepository.getById(roomid);
        LocalDateTime dateTime = LocalDateTime.of(day, LocalTime.of(0,0,0));
        LocalDateTime dateTime1 = LocalDateTime.of(day,LocalTime.of(23,59,59));

        Optional<List<Reservation>> reservationsByDay = reservationRepository.findByRoomByDay(room, dateTime, dateTime1);
        if(reservationsByDay.isPresent()){
            return reservationsByDay.get();
        }
        else {
            return new ArrayList<>();
        }
    }

    @Transactional
    public List<Reservation> addRecursiveReservations(ReservationRecursiveTO reservationRecursiveTO) throws OverlapErrorException {

        List<Reservation> reservationList = new ArrayList<>();
        LocalDate entryDate = reservationRecursiveTO.getEntryDate();

        while(entryDate.isBefore(reservationRecursiveTO.getFinalDate())){
            Reservation reservation = new Reservation();
            reservation.setDescription(reservationRecursiveTO.getDescription());

            LocalDateTime localDateTimeEntry = LocalDateTime.of(entryDate, reservationRecursiveTO.getEntryTime());
            LocalDateTime localDateTimeExit = LocalDateTime.of(entryDate, reservationRecursiveTO.getExitTime());
            reservation.setStart(localDateTimeEntry);
            reservation.setEnd(localDateTimeExit);

            Reservation reservation1 = addReservation(reservation);

            reservationList.add(reservation1);
            entryDate = entryDate.plusDays(7);
        }


        return reservationList;
    }

    public List<String> getAvailableTimeByRoomByDay(Integer roomid, String day){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/d/yyyy");

        LocalDate myDay = LocalDate.parse(day, formatter);

        List<Reservation> reservationsByDay = getReservationsByRoomByDay(roomid, myDay);
        List<String> availableTimeTO = new ArrayList<>();
        LocalTime localTime = LocalTime.of(8,0,0);

        while(localTime.isBefore(LocalTime.of(20,0,1))){
            Boolean flag = true;
            for(Reservation reservation : reservationsByDay){
                if((localTime.isAfter(reservation.getStart().toLocalTime())
                        && localTime.isBefore(reservation.getEnd().toLocalTime()))
                        || !localTime.equals(reservation.getStart().toLocalTime())){
                    flag = false;
                }
            }
            if(flag){
                availableTimeTO.add(localTime.toString());
            }
            localTime.plusMinutes(30);
        }

        return availableTimeTO;
    }

}
