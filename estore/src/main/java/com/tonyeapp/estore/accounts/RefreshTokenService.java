package com.tonyeapp.estore.accounts;

import java.time.Instant;
import java.util.Optional;

import org.springframework.stereotype.Service;

// store our refresh token in a database
@Service
public class RefreshTokenService {

     private final RefreshTokenRepository refreshTokenRepository;
     private final JwtService jwtService;

     public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, JwtService jwtService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
     }

     // create and save refresh token in db
       public RefreshToken createRefreshToken(UserInfo user) {
        String token = jwtService.generateRefreshToken(user.getEmail());

         RefreshToken refreshToken = new  RefreshToken();
         refreshToken.setUserInfo(user);
         refreshToken.setToken(token);
         refreshToken.setExpiryDate(Instant.now().plusMillis(7 * 24 * 60 * 60 * 1000)); // 7 days
        
         return refreshTokenRepository.save(refreshToken);
       }

       public Optional<RefreshToken> findByToken(String token) {
         return refreshTokenRepository.findByToken(token);
     } 

       // Validate token from DB
        public boolean validateRefreshToken(String token) {
            Optional<RefreshToken> stored =  refreshTokenRepository.findByToken(token);
            
            if(stored.isEmpty()) return false;
            if(stored.get().getExpiryDate().isBefore(Instant.now())) return false;

            return jwtService.validateRefreshToken(token);            
        }

        public void deleteToken(UserInfo user) {
            refreshTokenRepository.deleteByUserInfo(user);
        }

        // delete expired refresh token from the db 
        public void deleteExpiredTokens() {
           refreshTokenRepository.deleteByExpiryDateBefore(Instant.now());
        }
     
}
