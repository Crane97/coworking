package com.monforte.coworking.repositories;

import com.monforte.coworking.domain.entities.Reservation;
import com.monforte.coworking.domain.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    List<Reservation> findByRoom(Room room);

    //TODO: Probarlo en el DBEAVER
    @Query(value = "SELECT * FROM RESERVATION WHERE start between :dateTime1 AND :dateTime2", nativeQuery = true)
    Optional<List<Reservation>> findByRoomByDay(Room room, LocalDateTime dateTime1, LocalDateTime dateTime2);

}
