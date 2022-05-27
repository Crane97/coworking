package com.monforte.coworking.repositories;

import com.monforte.coworking.domain.entities.Reservation;
import com.monforte.coworking.domain.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    @Query(value = "SELECT * FROM RESERVATION ORDER BY start", nativeQuery = true)
    List<Reservation> findAllOrderByStart();

    List<Reservation> findByRoom(Room room);

    List<Reservation> findByUserIdOrderByStart(Integer id);

    @Query(value = "SELECT * FROM RESERVATION res, ROOM room WHERE res.id_room = room.id AND room.id = (:roomId) AND res.start between (:dateTime1) AND (:dateTime2)", nativeQuery = true)
    Optional<List<Reservation>> findByRoomByDay(@Param("roomId") Integer room, @Param("dateTime1") LocalDateTime dateTime1, @Param("dateTime2") LocalDateTime dateTime2);

}
