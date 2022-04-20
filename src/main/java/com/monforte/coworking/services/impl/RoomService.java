package com.monforte.coworking.services.impl;

import com.monforte.coworking.domain.dto.FreeReservationTO;
import com.monforte.coworking.domain.entities.Room;
import com.monforte.coworking.repositories.RoomRepository;
import com.monforte.coworking.services.IRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class RoomService implements IRoomService {

    @Autowired
    public RoomRepository roomRepository;

    public Page<Room> getRooms(Pageable pageable) {
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

    public FreeReservationTO getFreeTimeFromRoom(Integer id){
        FreeReservationTO freeReservationTO = new FreeReservationTO();
        String[] array = {"hola", "adios"};
        freeReservationTO.setFree(array);

        Optional<Room> room = roomRepository.findById(id);

        if(room.isPresent()){
            //Si existe la sala, vamos a tener que sacar sus reservations, y sacar los horarios por los que esté reservado.


        }

        return null;

    }
}
