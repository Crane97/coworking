package com.monforte.coworking.domain.dto.responses;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class AvailableTimeTO {

    private LocalDate day;

    private List<LocalTime> available;
}
