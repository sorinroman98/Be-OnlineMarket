package com.springapp.springjwt.repository;

import com.springapp.springjwt.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByEmail(String email);

    @Query("FROM User WHERE isActive = FALSE and role = 'ROLE_USER'")
    List<User> getAllInactiveAccount();
}
