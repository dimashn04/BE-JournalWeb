package com.trisakti.journalweb.dto;

import java.util.Set;
import java.util.UUID;

import com.trisakti.journalweb.model.ERole;

import lombok.Data;

@Data
public class UserDto {
    private UUID id;
    private String username;
    private String email;
    private String givenName;
    private String familyName;
    private String fullName;
    private String affiliation;
    private String country;
    private String phoneNumber;
    private Set<ERole> roles;
}
