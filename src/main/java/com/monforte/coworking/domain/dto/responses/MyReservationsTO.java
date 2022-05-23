package com.monforte.coworking.domain.dto.responses;

import com.monforte.coworking.domain.entities.Room;
import com.monforte.coworking.domain.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MyReservationsTO {

    public String description;

    public String date;

    public String start;

    public String end;

    public String status;

    public String place;

    public User user;

    public Room room;

}
