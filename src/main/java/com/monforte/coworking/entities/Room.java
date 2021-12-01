package com.monforte.coworking.entities;

import com.monforte.coworking.entities.enums.RoomType;
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

    @OneToMany
    private List<Reservation> reservation;
}
