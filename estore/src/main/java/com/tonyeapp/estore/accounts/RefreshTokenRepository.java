package com.tonyeapp.estore.accounts;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshTokenRepository extends JpaRepository <RefreshToken, Long>{
    Optional<RefreshToken>findByToken(String token);

    @Modifying
    @Transactional
    void deleteByUserInfo(UserInfo userInfo);


    @Modifying
    @Transactional
    void deleteByExpiryDateBefore(Instant date);

}


