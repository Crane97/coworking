package com.monforte.coworking.repositories;

import com.monforte.coworking.domain.entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

    Optional<Invoice> findFirstByReservationsId(Integer id);

}
