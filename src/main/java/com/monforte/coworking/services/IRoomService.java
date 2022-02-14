package com.monforte.coworking.services;

import com.monforte.coworking.domain.entities.Room;

import java.util.List;

public interface IRoomService {

    List<Room> getRooms();

    Room getRoomById(Integer id);

    Room addRoom(Room room);

    Room updateRoom(Room room);

    void deleteRoom(Integer id);
}
