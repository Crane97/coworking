package com.monforte.coworking.services.impl;

import com.monforte.coworking.domain.entities.Room;
import com.monforte.coworking.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class RoomService {

    @Autowired
    public RoomRepository roomRepository;

    public List<Room> getRooms() { return roomRepository.findAll(); }

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
}
