package com.monforte.coworking.controller;

import com.monforte.coworking.domain.entities.Room;
import com.monforte.coworking.services.impl.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(path = "/api/room")
public class RoomController {

    @Autowired
    public RoomService roomService;

    @GetMapping(path = "/rooms")
    public ResponseEntity<List<Room>> getRooms(){
        return new ResponseEntity<>(roomService.getRooms(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Integer id){
        Room room1 = roomService.getRoomById(id);
        return new ResponseEntity<>(room1, HttpStatus.OK);
    }

    @PostMapping(path = "/add")
    public ResponseEntity<Room> addRoom(@RequestBody Room room){
        Room room1 = roomService.addRoom(room);
        return new ResponseEntity<>(room1, HttpStatus.CREATED);
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<Room> updateRoom(@RequestBody Room request, @PathVariable Integer id){
        Room room1 = roomService.getRoomById(id);

        if(request.getName() != null) room1.setName(request.getName());
        if(request.getCapacity() != null) room1.setCapacity(request.getCapacity());
        if(request.getRoomType() != null) room1.setRoomType(request.getRoomType());

        Room room = roomService.updateRoom(room1);
        return new ResponseEntity<>(room, HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable Integer id){
        roomService.deleteRoom(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
