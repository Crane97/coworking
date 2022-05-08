package com.monforte.coworking.domain.dto.requests;

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

    public String description;

    public LocalDate entryDate;

    public LocalDate finalDate;

    public LocalTime entryTime;

    public LocalTime exitTime;

    public Integer places;

}
