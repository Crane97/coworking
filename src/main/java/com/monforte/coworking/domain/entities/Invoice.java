package com.monforte.coworking.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "INVOICE")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NUMBER")
    private String number;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "TOTAL_AMOUNT")
    private double totalAmount;

    @DateTimeFormat(pattern = "MM/dd/yyyy")
    @Column(name = "ISSUED")
    private LocalDateTime issued;

    @OneToMany(mappedBy = "groupedInvoices")
    @JsonIgnore
    private GroupedInvoices invoice;

}
