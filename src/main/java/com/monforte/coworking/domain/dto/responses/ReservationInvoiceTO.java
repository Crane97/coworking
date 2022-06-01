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
public class ReservationInvoiceTO {

    public Integer id;

    public String description;

    public double totalTime;

    public String status;

    public String place;

    public double totalAmount;

    public double discount;

    public double finalAmount;

    public User user;

    public Room room;

}
