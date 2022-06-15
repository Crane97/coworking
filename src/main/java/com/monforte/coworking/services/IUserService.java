package com.monforte.coworking.services;

import com.monforte.coworking.domain.entities.Role;
import com.monforte.coworking.domain.entities.User;
import com.monforte.coworking.exceptions.DuplicatedUserException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.NoSuchElementException;

public interface IUserService {

    Page<User> getUsers(Pageable pageable);

    Page<User> getPublicableUsers(Pageable pageable);

    User getUser(Integer id) throws NoSuchElementException;

    User getUserByCustomer(String customer) throws NoSuchElementException;

    User addUser(User user) throws DuplicatedUserException;

    User updateUser(User user);

    void deleteUser(Integer id);

    Role saveRole(Role role);

    void addRoleToUser(String username, String roleName);

    User getUserByUsername(String username);
}
