package com.monforte.coworking.repositories;

import com.monforte.coworking.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    public void deleteById(Integer id);
}
