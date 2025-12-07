package com.tonyeapp.estore.accounts;

import lombok.AllArgsConstructor;

import lombok.Data;

@Data
@AllArgsConstructor
public class AuthRequestDTO {
    private String username;
    private String password;
}
