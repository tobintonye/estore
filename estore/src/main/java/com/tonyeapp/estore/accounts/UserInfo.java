package com.tonyeapp.estore.accounts;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class UserInfo {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private String firstname;
    private String lastname;
    @Column(unique=true, nullable=false)
    @Email(message="Please provide a valid email address")
    @NotBlank(message="Email cannot be blank")
    @Size(max=255)
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

     public UserInfo(int id, String firstname, String lastname, String email, String password,  UserRole userRole) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }
    
     public UserInfo() {}
}
