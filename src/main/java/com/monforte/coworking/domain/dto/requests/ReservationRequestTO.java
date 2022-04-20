package com.monforte.coworking.domain.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequestTO {

    private Integer id;

    private String description;

    private String start;

    private String end;

    private String status;

    private String place;

}
