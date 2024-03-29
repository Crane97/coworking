package com.monforte.coworking.services;

import com.monforte.coworking.domain.dto.FreeReservationTO;
import com.monforte.coworking.domain.entities.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface IRoomService {

    Page<Room> getRooms(Pageable pageable);

    Room getRoomById(Integer id);

    Room addRoom(Room room);

    Room updateRoom(Room room);

    void deleteRoom(Integer id);

    FreeReservationTO getFreeTimeFromRoomByDay(Integer id, LocalDate date);
}
