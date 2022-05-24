package com.monforte.coworking.domain.dto.responses;

import com.monforte.coworking.domain.entities.Reservation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRecursiveResponseTO {

    private List<Reservation> reservations;

    private String errors;
}
