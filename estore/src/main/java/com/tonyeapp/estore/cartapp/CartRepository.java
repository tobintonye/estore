package com.tonyeapp.estore.cartapp;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tonyeapp.estore.accounts.UserInfo;

public interface CartRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByUserInfo(UserInfo user);

}
