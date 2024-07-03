package com.trisakti.journalweb.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "users",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
    })
public class UserEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;

    @Size(max = 400)
    private String fullName;

    @NotBlank
    @Size(max = 200)
    private String givenName;

    @NotBlank
    @Size(max = 200)
    private String familyName;

    @NotBlank
    @Size(max = 200)
    private String affiliation;

    @NotBlank
    @Size(max = 200)
    private String country;

    @NotBlank
    @Size(max = 15)
    private String phoneNumber;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public UserEntity() {
    }

    public UserEntity(String username, String email, String password, String givenName, String familyName, String affiliation, String country, String phoneNumber) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.givenName = givenName;
        this.familyName = familyName;
        this.affiliation = affiliation;
        this.country = country;
        this.phoneNumber = phoneNumber;
    }
}
