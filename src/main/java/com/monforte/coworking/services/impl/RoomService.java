package com.monforte.coworking.services.impl;

import com.monforte.coworking.domain.dto.FreeReservationTO;
import com.monforte.coworking.domain.entities.Reservation;
import com.monforte.coworking.domain.entities.Room;
import com.monforte.coworking.repositories.RoomRepository;
import com.monforte.coworking.services.IRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class RoomService implements IRoomService {

    @Autowired
    public RoomRepository roomRepository;

    public Page<Room> getRooms(Pageable pageable) {
        FreeReservationTO freeReservationTO = getFreeTimeFromRoomByDay(1, LocalDate.of(2015, 02, 20));
        return (Page<Room>) roomRepository.findAll(pageable);
    }

    public Room getRoomById(Integer id) throws NoSuchElementException{

        Optional<Room> room = roomRepository.findById(id);

        if(room.isPresent()) {
            return room.get();
        }
        else throw new NoSuchElementException("No room with id: " +id);
    }

    public Room addRoom(Room room){ return roomRepository.save(room); }

    public Room updateRoom(Room room){ return roomRepository.save(room); }

    public void deleteRoom(Integer id){ roomRepository.deleteById(id); }

    public FreeReservationTO getFreeTimeFromRoomByDay(Integer id, LocalDate date){
        FreeReservationTO freeReservationTO = new FreeReservationTO();
        String[] array = {"8:00", "8:30"};
        freeReservationTO.setFree(array);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        Optional<Room> room = roomRepository.findById(id);

        if(room.isPresent()){
            //Si existe la sala, vamos a tener que sacar sus reservations, y sacar los horarios por los que esté reservado.

            for(Reservation reservation : room.get().getReservation()){
                LocalDateTime startDateTime = reservation.getStart();
                LocalDateTime endDateTime = reservation.getEnd();

                String start = startDateTime.format(formatter);
                String end = endDateTime.format(formatter);

            }

        }

        return null;

    }
}
