package com.trisakti.journalweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trisakti.journalweb.dto.ProfileEditDto;
import com.trisakti.journalweb.dto.UserDto;
import com.trisakti.journalweb.security.JwtGenerator;
import com.trisakti.journalweb.service.UserService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    private JwtGenerator jwtGenerator;
    private UserService userService;

    @Autowired
    public ProfileController(JwtGenerator jwtGenerator, UserService userService) {
        this.jwtGenerator = jwtGenerator;
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<UserDto> getProfile(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String username = retriveUsernameFromToken(token);

        try {
            UserDto user = userService.findByUsername(username);
            return ResponseEntity.ok(user);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<Object> editProfile(@RequestBody ProfileEditDto profileEditDto, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String username = retriveUsernameFromToken(token);

        try {
            userService.update(username, profileEditDto);
            return ResponseEntity.ok().build();
        } catch(IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input: " + e.getMessage());
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error: " + e.getMessage());
        }
    }

    private String retriveUsernameFromToken(String token) {
        String jwtToken = token.substring(7);
        return jwtGenerator.getUsernameFromJwt(jwtToken);
    }
}
