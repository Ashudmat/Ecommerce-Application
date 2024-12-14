package org.ecom.microservice.userservice.service;

import org.ecom.microservice.userservice.dto.UserDto;
import org.ecom.microservice.userservice.exception.UserAlreadyExistsException;
import org.ecom.microservice.userservice.mapper.UserMapper;
import org.ecom.microservice.userservice.model.User;
import org.ecom.microservice.userservice.repository.RoleRepository;
import org.ecom.microservice.userservice.repository.SessionRepository;
import org.ecom.microservice.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.ReactiveOffsetScrollPositionHandlerMethodArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SessionRepository sessionRepository;

    @Autowired
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthenticationService(UserRepository userRepository, RoleRepository roleRepository, SessionRepository sessionRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.sessionRepository = sessionRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    public UserDto signUp(String email, String password) {
    Optional<User> checkUser= userRepository.findByEmail(email);  //check if email already exists in db
        if(!checkUser.isEmpty())
            throw new UserAlreadyExistsException("User already exists with the associated email! Please login to continue");

        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));

        userRepository.save(user);
        return new ResponseEntity<>(UserMapper.userToDtoMapper(user), HttpStatus.OK).getBody();

    }

}
