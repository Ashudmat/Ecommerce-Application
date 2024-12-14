package org.ecom.microservice.userservice.controller;

import org.ecom.microservice.userservice.dto.LoginRequestDto;
import org.ecom.microservice.userservice.dto.SignUpRequestDto;
import org.ecom.microservice.userservice.dto.UserDto;
import org.ecom.microservice.userservice.repository.SessionRepository;
import org.ecom.microservice.userservice.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    //to be implemented here:
    //signup
    //login
    //validate token
    //logout
    //get all sessions
    //get user based on token

    private final AuthenticationService authenticationService;
    private final SessionRepository sessionRepository;

    public AuthenticationController(AuthenticationService authenticationService, SessionRepository sessionRepository) {
        this.authenticationService = authenticationService;
        this.sessionRepository = sessionRepository;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpRequestDto request){
        UserDto userDto = authenticationService.signUp(request.getEmail(), request.getPassword());
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

}
