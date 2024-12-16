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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Map<String,Object>> signUp(@RequestBody SignUpRequestDto request) {
        UserDto userDto = authenticationService.signUp(request.getEmail(), request.getPassword());

        Map<String,Object> response = new HashMap<>();
        response.put("message","User signed up successfully");
        response.put("user",userDto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequestDto request) {
        UserDto userDto = authenticationService.login(request.getEmail(), request.getPassword());

        Map<String,Object> response = new HashMap<>();
        response.put("message","User logged in successfully");
        response.put("user",userDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/validate")
    public ResponseEntity<SessionStatus> validateToken(@RequestBody ValidateTokenRequest request) throws UnsupportedEncodingException {
        SessionStatus status = authenticationService.validateToken(request.getToken(), request.getUserId());
        return new ResponseEntity<>(status,HttpStatus.OK);
    }

    @PostMapping(path = "/logout")
    public ResponseEntity<String> logout(@RequestParam Long userId, @RequestHeader("token") String token) {
        String res = authenticationService.logout(token , userId);
        log.info(res);
        return ResponseEntity.ok(res);
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

