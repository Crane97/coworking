package com.monforte.coworking.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "id_admin")
    private Integer idAdmin;

    @Column(name = "name_admin")
    private String nameAdmin;

    @OneToMany(mappedBy = "company")
    @JsonIgnore
    private List<User> userList;


    public Company(String name, String workers, String field, String logo, Boolean hiring, Integer idAdmin, String nameAdmin) {
        this.name = name;
        this.workers = workers;
        this.field = field;
        this.logo = logo;
        this.hiring = hiring;
        this.idAdmin = idAdmin;
        this.nameAdmin = nameAdmin;
        this.userList = new ArrayList<>();
    }
}
