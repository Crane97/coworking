package com.monforte.coworking.services;

import com.monforte.coworking.domain.entities.Role;
import com.monforte.coworking.domain.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.NoSuchElementException;

public interface IUserService {

    Page<User> getUsers(Pageable pageable);

    User getUser(Integer id) throws NoSuchElementException;

    User addUser(User user);

    User updateUser(User user);

    void deleteUser(Integer id);

    Role saveRole(Role role);

    void addRoleToUser(String username, String roleName);
}
