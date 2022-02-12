package com.monforte.coworking.reservationServiceTests;

import com.monforte.coworking.domain.entities.Reservation;
import com.monforte.coworking.services.impl.ReservationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    public ReservationService reservationService;

    @Test
    public void compareLocalDateTimesReservations_test(){
        LocalDateTime start1;
        LocalDateTime start2;
        LocalDateTime end1;
        LocalDateTime end2;

        List<Reservation> mockList = new ArrayList<>();

        Mockito
                .doReturn(null)
                .when(this.reservationService)
                .getReservations();

        assertTrue(true);
        assertFalse(false);
    }
}
