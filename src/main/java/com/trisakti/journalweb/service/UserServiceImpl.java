package com.trisakti.journalweb.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.trisakti.journalweb.dto.AuthResponseDto;
import com.trisakti.journalweb.dto.LoginDto;
import com.trisakti.journalweb.dto.ProfileEditDto;
import com.trisakti.journalweb.dto.RegisterDto;
import com.trisakti.journalweb.dto.UserDto;
import com.trisakti.journalweb.exception.EmailAlreadyExistsException;
import com.trisakti.journalweb.exception.UsernameAlreadyExistsException;
import com.trisakti.journalweb.model.ERole;
import com.trisakti.journalweb.model.Role;
import com.trisakti.journalweb.model.UserEntity;
import com.trisakti.journalweb.repository.RoleRepository;
import com.trisakti.journalweb.repository.UserRepository;
import com.trisakti.journalweb.security.JwtGenerator;
import com.trisakti.journalweb.security.UserDetailsImpl;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;
    private AuthenticationManager authenticationManager;
    private JwtGenerator jwtGenerator;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, AuthenticationManager authenticationManager, JwtGenerator jwtGenerator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.jwtGenerator = jwtGenerator;
    }

    @Override
    public UserEntity create(RegisterDto user) throws RuntimeException {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        UserEntity newUser = new UserEntity(user.getUsername(),
            user.getEmail(),
            passwordEncoder.encode(user.getPassword()),
            user.getGivenName(),
            user.getFamilyName(),
            user.getAffiliation(),
            user.getCountry(),
            user.getPhoneNumber());

        Set<String> strRoles = user.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                if (role.equals("admin")) {
                    throw new RuntimeException("You cannot register as an admin");
                } else {
                    Role userRole = null;
                    userRole = roleRepository.findByName(ERole.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(userRole);
                }
            });
        }

        newUser.setRoles(roles);
        userRepository.save(newUser);

        return newUser;
    }

    @Override
    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDto findByUsername(String username) {
        UserEntity user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return convertToDto(user);
    }

    @Override
    public void update(String username, ProfileEditDto data) throws UsernameNotFoundException, IllegalArgumentException {
        UserEntity user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        System.out.println("New PW: " + data.getNewPassword().isEmpty());
        if (data.getNewPassword().isEmpty() == false && data.getNewPassword() != null){
            if (data.getOldPassword() == null || data.getOldPassword().isEmpty()) {
                throw new IllegalArgumentException("Old password is required");
            }

            if (!passwordEncoder.matches(data.getOldPassword(), user.getPassword())) {
                throw new IllegalArgumentException("Old password is incorrect");
            }

            user.setPassword(passwordEncoder.encode(data.getNewPassword()));
        }

        if (data.getGivenName().isEmpty() == false && data.getGivenName() != null){
            user.setGivenName(data.getGivenName());
        }

        if (data.getFamilyName().isEmpty() == false && data.getFamilyName() != null){
            user.setFamilyName(data.getFamilyName());
        }

        if (data.getAffiliation().isEmpty() == false && data.getAffiliation() != null){
            user.setAffiliation(data.getAffiliation());
        }

        if (data.getCountry().isEmpty() == false && data.getCountry() != null){
            user.setCountry(data.getCountry());
        }

        if (data.getPhoneNumber().isEmpty() == false && data.getPhoneNumber() != null){
            user.setPhoneNumber(data.getPhoneNumber());
        }

        if (data.getFullName().isEmpty() == false && data.getFullName() != null){
            user.setFullName(data.getFullName());
        }

        userRepository.save(user);
    }

    @Override
    public AuthResponseDto login(LoginDto user) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                user.getPassword()
            )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());

        return new AuthResponseDto(token, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles);
    }

    private UserDto convertToDto(UserEntity user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setGivenName(user.getGivenName());
        userDto.setFamilyName(user.getFamilyName());
        userDto.setFullName(user.getFullName());
        userDto.setAffiliation(user.getAffiliation());
        userDto.setCountry(user.getCountry());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
        return userDto;
    }
}
