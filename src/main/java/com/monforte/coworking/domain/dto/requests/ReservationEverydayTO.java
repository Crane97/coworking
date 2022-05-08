package com.monforte.coworking.domain.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationEverydayTO {

    public String description;

    public LocalDate entryDate;

    public LocalDate finalDate;

    public String entryTime;

    public String exitTime;

    public Integer places;

}
