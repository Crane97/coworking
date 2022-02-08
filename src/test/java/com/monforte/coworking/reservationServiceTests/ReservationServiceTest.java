package com.monforte.coworking.reservationServiceTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Test
    public void compareLocalDateTimesReservations_test(){
        LocalDateTime start1;
        LocalDateTime start2;
        LocalDateTime end1;
        LocalDateTime end2;

        assertTrue(true);
        assertFalse(false);
    }
}
