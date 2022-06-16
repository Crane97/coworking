package com.monforte.coworking.domain.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "COMPANY")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "WORKERS")
    private String workers;

    @Column(name = "FIELD")
    private String field;

    @Column(name = "LOGO")
    private String logo;

    @Column(name = "HIRING")
    private Boolean hiring;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User idAdmin;

    public Company(String name, String workers, String field, String logo, Boolean hiring, User idAdmin) {
        this.name = name;
        this.workers = workers;
        this.field = field;
        this.logo = logo;
        this.hiring = hiring;
        this.idAdmin = idAdmin;
    }
}
