package com.monforte.coworking.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.monforte.coworking.domain.entities.enums.RoomType;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "ROOM")
@Inheritance(strategy = InheritanceType.JOINED)
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NAME")
    @NotNull
    private String name;

    @Column(name = "CAPACITY")
    private Integer capacity;

    @Column(name = "ROOMTYPE")
    private RoomType roomType;

    @OneToMany(mappedBy = "room")
    @JsonIgnore
    private List<Reservation> reservation;

    public Room(String name, Integer capacity, RoomType roomType, List<Reservation> reservation) {
        this.name = name;
        this.capacity = capacity;
        this.roomType = roomType;
        this.reservation = reservation;
    }
}
