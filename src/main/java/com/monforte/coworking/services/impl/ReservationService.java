package com.monforte.coworking.services.impl;

import com.monforte.coworking.domain.dto.requests.ReservationRecursiveTO;
import com.monforte.coworking.domain.dto.requests.ReservationRequestTO;
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

        LocalDateTime dateTime = LocalDateTime.of(day, LocalTime.of(0,0,0));
        LocalDateTime dateTime1 = LocalDateTime.of(day,LocalTime.of(23,59,59));

        Optional<List<Reservation>> reservationsByDay = reservationRepository.findByRoomByDay(roomid, dateTime, dateTime1);

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

    public List<LocalTime> getAvailableTimeByRoomByDay(Integer roomid, LocalDate day){

        List<Reservation> reservationsByDay = getReservationsByRoomByDay(roomid, day);
        List<LocalTime> availableTimeTO = new ArrayList<>();
        LocalTime localTime = LocalTime.of(8,0,0);

        while(localTime.isBefore(LocalTime.of(20,0,1))){
            Boolean flag = true;
            for(Reservation reservation : reservationsByDay){
                if((localTime.isAfter(reservation.getStart().toLocalTime())
                        && localTime.isBefore(reservation.getEnd().toLocalTime()))
                        || localTime.equals(reservation.getStart().toLocalTime())){
                    flag = false;
                }
            }
            if(flag){
                availableTimeTO.add(localTime);
            }
            localTime = localTime.plusMinutes(30);
        }

        return availableTimeTO;
    }

    public Reservation addNormalReservation(ReservationRequestTO reservationTO) throws OverlapErrorException{

        Reservation reservation = new Reservation();

        String[] splitStart = reservationTO.getStart().split(":");
        String[] splitEnd = reservationTO.getEnd().split(":");


        LocalDateTime start = LocalDateTime.of(reservationTO.getDate().getYear(), reservationTO.getDate().getMonth(), reservationTO.getDate().getDayOfMonth(), Integer.parseInt(splitStart[0]), Integer.parseInt(splitStart[1]));
        LocalDateTime end = LocalDateTime.of(reservationTO.getDate().getYear(), reservationTO.getDate().getMonth(), reservationTO.getDate().getDayOfMonth(), Integer.parseInt(splitEnd[0]), Integer.parseInt(splitEnd[1]));

        if(reservationTO.getDescription()!=null) reservation.setDescription(reservationTO.getDescription());
        if(reservationTO.getDate()!=null) reservation.setDescription(reservationTO.getDescription());
        if(reservationTO.getStart()!=null) reservation.setStart(start);
        if(reservationTO.getEnd()!=null) reservation.setEnd(end);
        if(reservationTO.getStatus()!=null) reservation.setDescription(reservationTO.getDescription());
        if(reservationTO.getPlace()!=null) reservation.setDescription(reservationTO.getDescription());
        if(reservationTO.getQuantity()!=null) reservation.setDescription(reservationTO.getDescription());

        return addReservation(reservation);
    }


}
