package com.monforte.coworking.services.impl;

import com.monforte.coworking.domain.entities.Role;
import com.monforte.coworking.domain.entities.User;
import com.monforte.coworking.repositories.RoleRepository;
import com.monforte.coworking.repositories.UserRepository;
import com.monforte.coworking.services.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserService implements IUserService, UserDetailsService {

    //TODO MIRAR LO QUE HACE EL @REQUIREDARGSCONSTRUCTOR

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null){
            log.error("User not found in the Database");
            throw new UsernameNotFoundException("User not found in the Database");
        }
        else{
            log.info("User found in the Database: {}", username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    public Page<User> getUsers(Pageable pageable){
        return (Page<User>) userRepository.findAll(pageable);
    }

    public Page<User> getPublicableUsers(Pageable pageable){
        return (Page<User>) userRepository.findByPublicable(pageable);
    }

    public User getUser(Integer id) throws NoSuchElementException{
        Optional<User> user = userRepository.findById(id);

        if(user.isPresent()){
            return user.get();
        }
        else throw new NoSuchElementException("No User with id: " + id);
    }

    public User addUser(User user){
        log.info("Saving user on the database: {}", user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //Si dejamos que Reservation se inicialice a null, nos saltará posteriormente un NullPointerException
        user.setReservation(new ArrayList<>());
        return userRepository.save(user);
    }

    public User updateUser(User user){
        return userRepository.save(user);
    }

    public void deleteUser(Integer id){
        userRepository.deleteById(id);
    }

    public Role saveRole(Role role){
        return roleRepository.save(role);
    }

    public void addRoleToUser(String username, String roleName){
        User user = userRepository.findByUsername(username);
        Role role = roleRepository.findByName(roleName);
        user.getRoles().add(role);
    }
}
