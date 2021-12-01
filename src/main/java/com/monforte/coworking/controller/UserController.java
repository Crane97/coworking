package com.monforte.coworking.controller;

import com.monforte.coworking.entities.User;
import com.monforte.coworking.exceptions.ApiErrorException;
import com.monforte.coworking.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/user")
public class UserController {

    @Autowired
    public UserService userService;

    @GetMapping(path = "/users")
    public ResponseEntity<Page<User>> getAllUsers(@RequestParam(defaultValue = "0") Integer page){
        Page<User> user1 = userService.getUsers(PageRequest.of(page, 500));
        return new ResponseEntity<>(user1,HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") Integer id) {
        User user1 = userService.getUser(id);
        return new ResponseEntity<>(user1, HttpStatus.OK);
    }

    @PostMapping(path = "/add")
    public ResponseEntity<User> addUser(@RequestBody User user){
        User user1 = userService.addUser(user);
        return new ResponseEntity<>(user1, HttpStatus.CREATED);
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<?> updateUser(@RequestBody User request, @PathVariable Integer id){
        try {
            User user = userService.getUser(id);

            if(user == null) {
                throw new ApiErrorException("User with id: " + id + " not found.");
            }

            if (request.getName() != null) user.setName(request.getName());
            if (request.getSurname() != null) user.setSurname(request.getSurname());
            if (request.getEmail() != null) user.setEmail(request.getEmail());
            if (request.getPhone() != null) user.setPhone(request.getPhone());
            if (request.getPartner() != null) user.setPartner(request.getPartner());
            if (request.getAccount() != null) user.setAccount(request.getAccount());
            if (request.getPassword() != null) user.setPassword(request.getPassword());
            if (request.getOpenToWork() != null) user.setOpenToWork(request.getOpenToWork());
            if (request.getJobTitle() != null) user.setJobTitle(request.getJobTitle());
            if (request.getPublicable() != null) user.setPublicable(request.getPublicable());
            if (request.getDescription() != null) user.setDescription(request.getDescription());

            User user1 = userService.updateUser(user);
            return new ResponseEntity<>(user1, HttpStatus.CREATED);
        }
        catch(ApiErrorException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Integer id){
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
