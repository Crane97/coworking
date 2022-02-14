package com.monforte.coworking.services.impl;

import com.monforte.coworking.domain.entities.User;
import com.monforte.coworking.repositories.UserRepository;
import com.monforte.coworking.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    public UserRepository userRepository;

    public Page<User> getUsers(Pageable pageable){
        return (Page<User>) userRepository.findAll(pageable);
    }

    public User getUser(Integer id) throws NoSuchElementException{
        Optional<User> user = userRepository.findById(id);

        if(user.isPresent()){
            return user.get();
        }
        else throw new NoSuchElementException("No User with id: " + id);
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
