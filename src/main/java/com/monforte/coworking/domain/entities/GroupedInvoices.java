package com.monforte.coworking.domain.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "groupedInvoices")
public class GroupedInvoices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    //Stripe no cuenta en monedas, cuenta en centimos
    @Column(name = "TOTAL_AMOUNT")
    private int totalAmount;

    @Column(name = "currency")
    private String currency;

    @DateTimeFormat(pattern = "MM/dd/yyyy")
    @Column(name = "ISSUED")
    private LocalDateTime issued;

    @OneToMany(mappedBy = "invoice")
    private List<Reservation> reservations;

    @ManyToOne
    @JoinColumn(name = "id_room")
    private Invoice invoice;
}
