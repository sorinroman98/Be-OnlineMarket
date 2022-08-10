package com.springapp.springjwt.repository;

import com.springapp.springjwt.domain.User;
import com.springapp.springjwt.domain.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public interface VerificationTokenRepository  extends JpaRepository<VerificationToken,Long> {
    VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);

    VerificationToken deleteByUser(User user);

    @Transactional
    @Modifying
    @Query("DELETE FROM VerificationToken v WHERE v.expiryDate < :date AND v.user.id = :id")
    int deleteExpiredVerificationTokenWithUnconfirmedUserAccount(@Param(value = "date") Date date, @Param(value = "id") int id);
}
