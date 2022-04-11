package com.monforte.coworking.repositories;

import com.monforte.coworking.domain.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    public void deleteById(Integer id);

    public User findByUsername(String username);

    @Query(value = "SELECT * FROM user WHERE PUBLICABLE = 1", nativeQuery = true)
    public Page<User> findByPublicable(Pageable pageable);
}
