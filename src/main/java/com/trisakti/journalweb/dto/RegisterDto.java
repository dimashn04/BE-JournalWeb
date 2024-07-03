package com.trisakti.journalweb.dto;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private Set<String> role;

    @NotBlank
    @Size(min = 8, max = 40)
    private String password;

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
}
