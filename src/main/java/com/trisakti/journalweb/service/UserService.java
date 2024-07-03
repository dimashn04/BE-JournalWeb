package com.trisakti.journalweb.service;

import java.util.List;

import com.trisakti.journalweb.dto.AuthResponseDto;
import com.trisakti.journalweb.dto.LoginDto;
import com.trisakti.journalweb.dto.ProfileEditDto;
import com.trisakti.journalweb.dto.RegisterDto;
import com.trisakti.journalweb.dto.UserDto;
import com.trisakti.journalweb.model.UserEntity;

public interface UserService {
    public UserEntity create(RegisterDto user);
    public List<UserEntity> findAll();
    UserDto findByUsername(String username);
    public void update(String username, ProfileEditDto data);
    public AuthResponseDto login(LoginDto user);
}
