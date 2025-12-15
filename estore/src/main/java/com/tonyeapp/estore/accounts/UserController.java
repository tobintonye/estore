package com.tonyeapp.estore.accounts;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserInfoService service;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserInfoRepository userInfoRepository;

    @GetMapping("/estore")
    public String welcome() {
        return "Welcome to estore";
    }

    @PostMapping("/register")
    public String register(@RequestBody UserInfo userInfo) {
        return service.addUser(userInfo);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequestDTO authRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(authRequestDto.getUsername(), authRequestDto.getPassword())
        );

        if(authentication.isAuthenticated()) {
            // get your from the db
            UserInfo user = userInfoRepository.findByEmail(authRequestDto.getUsername())
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            // Delete old refresh token if exists
            refreshTokenService.deleteToken(user);

            String accessToken =  jwtService.generateAccessToken(user.getEmail());
            RefreshToken refreshToken =  refreshTokenService.createRefreshToken(user);
            
            return new AuthResponse(accessToken, refreshToken.getToken());
        } else {
            throw new UsernameNotFoundException("Invalid credentials");
        }
    }
    
    @PostMapping("/refresh-token")
    public AuthResponse refreshToken(@RequestBody RefreshTokenDTO request) {

        if(!refreshTokenService.validateRefreshToken(request.getToken())) {
            throw new RuntimeException("Invalid or expired refresh token");
        }
         // fetch refresh token from the db
        RefreshToken storedToken = refreshTokenRepository.findByToken(request.getToken()) 
        .orElseThrow(() -> new RuntimeException("Refresh token not found"));
   
        /*
        if(storedToken.getExpiryDate().isBefore(Instant.now())) {
            throw new  RuntimeException("Refresh token expired");
        }
    */
        String username = storedToken.getUserInfo().getEmail();
        
        String newAccessToken = jwtService.generateAccessToken(username);

        return new AuthResponse(newAccessToken, storedToken.getToken());
    }

    @PostMapping("/logout")
    public String logout(@RequestBody RefreshTokenDTO request) {
        RefreshToken storedToken = refreshTokenService.findByToken(request.getToken())
        .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        refreshTokenService.deleteToken(storedToken.getUserInfo());
        return "Logged out successfully";
    }
}