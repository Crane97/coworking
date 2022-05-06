package com.monforte.coworking.repositories;

import com.monforte.coworking.domain.entities.Reservation;
import com.monforte.coworking.domain.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    List<Reservation> findByRoom(Room room);

}
