package com.trisakti.journalweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileEditDto {
    private String oldPassword;
    private String newPassword;
    private String fullName;
    private String givenName;
    private String familyName;
    private String affiliation;
    private String country;
    private String phoneNumber;
}
