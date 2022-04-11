package com.monforte.coworking.domain.dto.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRequestTO {

    private String name;

    private String surname;

    private String email;

    private String phone;

    private String username;

    private String password;

    private String jobTitle;

    private String description;

    public UserRequestTO(String name, String surname, String email, String phone, String username, String password, String jobTitle, String description) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.jobTitle = jobTitle;
        this.description = description;
    }
}
