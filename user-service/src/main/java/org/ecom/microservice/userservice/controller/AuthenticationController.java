package org.ecom.microservice.userservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.ecom.microservice.userservice.dto.LoginRequestDto;
import org.ecom.microservice.userservice.dto.SignUpRequestDto;
import org.ecom.microservice.userservice.dto.UserDto;
import org.ecom.microservice.userservice.dto.ValidateTokenRequest;
import org.ecom.microservice.userservice.model.Session;
import org.ecom.microservice.userservice.model.SessionStatus;
import org.ecom.microservice.userservice.model.User;
import org.ecom.microservice.userservice.repository.SessionRepository;
import org.ecom.microservice.userservice.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@Slf4j
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

    @PostMapping(path = "/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpRequestDto request) {
        UserDto userDto = authenticationService.signUp(request.getEmail(), request.getPassword());
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto request) {
        UserDto userDto = authenticationService.login(request.getEmail(), request.getPassword());
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping(path = "/validate")
    public ResponseEntity<SessionStatus> validateToken(@RequestBody ValidateTokenRequest request) throws UnsupportedEncodingException {
        SessionStatus status = authenticationService.validateToken(request.getToken(), request.getUserId());
        return new ResponseEntity<>(status,HttpStatus.OK);
    }

    @PostMapping(path = "/{id}")
    public ResponseEntity<Void> logout(@PathVariable("id") Long userId, @RequestHeader("token") String token) {
        String res = authenticationService.logout(userId, token);
        log.info(res);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/sessions")
    public ResponseEntity<List<Session>> getAllSessions() {
        return authenticationService.getAllSessions();
    }

    @PostMapping(path = "/user")
    public ResponseEntity<User> getUser(@RequestParam String token){
        return authenticationService.getUser(token);
    }

}

