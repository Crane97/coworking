package com.monforte.coworking.services;

import com.monforte.coworking.entities.Room;
import com.monforte.coworking.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    @Autowired
    public RoomRepository roomRepository;

    public List<Room> getRooms() { return roomRepository.findAll(); }

    public Room getRoomById(Integer id){ return roomRepository.findById(id).get(); }

    public Room addRoom(Room room){ return roomRepository.save(room); }

    public Room updateRoom(Room room){ return roomRepository.save(room); }

    public void deleteRoom(Integer id){ roomRepository.deleteById(id); }
}
