package com.monforte.coworking.domain.dto.requests;

import com.monforte.coworking.domain.entities.Room;
import com.monforte.coworking.domain.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRecursiveTO {

    private String description;

    private LocalDate entryDate;

    private LocalDate finalDate;

    private String weekday;

    private String start;

    private String end;

    private String status;

    private String place;

    private Integer quantity;

    private Room room;

    private User user;

}
