package com.monforte.coworking.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SURNAME")
    private String surname;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "PARTNER")
    private Boolean partner;

    @Column(name = "ACCOUNT")
    @NotNull
    private String account;

    @Column(name = "PASSWORD")
    @NotNull
    private String password;

    @Column(name = "OPENTOWORK")
    private Boolean openToWork;

    @Column(name = "JOBTITLE")
    private String jobTitle;

    @Column(name = "PUBLICABLE")
    private Boolean publicable;

    @Column(name = "DESCRIPTION")
    private String description;

    @JsonIgnore
    @OneToOne
    private Company company;

    @OneToMany
    private List<Reservation> reservation;

    public User(String name, String surname, String email, String phone, Boolean partner, String account, String password, Boolean openToWork, String jobTitle, Boolean publicable, String description, Company company, List<Reservation> reservation) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.partner = partner;
        this.account = account;
        this.password = password;
        this.openToWork = openToWork;
        this.jobTitle = jobTitle;
        this.publicable = publicable;
        this.description = description;
        this.company = company;
        this.reservation = reservation;
    }
}
