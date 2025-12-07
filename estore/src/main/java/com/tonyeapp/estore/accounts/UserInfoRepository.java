package com.tonyeapp.estore.accounts;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
     Optional<UserInfo> findByEmail(String email);
}
