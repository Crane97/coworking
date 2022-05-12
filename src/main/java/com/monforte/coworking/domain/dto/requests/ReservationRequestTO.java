package com.monforte.coworking.domain.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequestTO {

    private Integer id;

    private String description;

    private LocalDateTime start;

    private LocalDateTime end;

    private String status;

    private String place;

    private Integer quantity;

}