package com.monforte.coworking.services;

import com.monforte.coworking.domain.entities.User;
import com.monforte.coworking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    public UserRepository userRepository;

    public Page<User> getUsers(Pageable pageable){
        return (Page<User>) userRepository.findAll(pageable);
    }

    public User getUser(Integer id){
        return userRepository.findById(id).get();
    }

    public User addUser(User user){
        return userRepository.save(user);
    }

    public User updateUser(User user){
        return userRepository.save(user);
    }

    public void deleteUser(Integer id){
        userRepository.deleteById(id);
    }

}
