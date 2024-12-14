package org.ecom.microservice.userservice.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.ecom.microservice.userservice.dto.UserDto;
import org.ecom.microservice.userservice.exception.*;
import org.ecom.microservice.userservice.mapper.UserMapper;
import org.ecom.microservice.userservice.model.Session;
import org.ecom.microservice.userservice.model.SessionStatus;
import org.ecom.microservice.userservice.model.User;
import org.ecom.microservice.userservice.repository.RoleRepository;
import org.ecom.microservice.userservice.repository.SessionRepository;
import org.ecom.microservice.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMapAdapter;

import javax.crypto.SecretKey;
import javax.swing.text.html.Option;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
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

    public UserDto login(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);  //get user from db

        if(userOptional.isEmpty()){   //check whether user found or not
            throw new UserNotFoundException("No user found with this email! Kindly sign up to continue");
        }

        User user = userOptional.get();

        if(!bCryptPasswordEncoder.matches(password, user.getPassword())){
            throw new InvalidCredentialsException("Entered password might be incorrect! Please enter correct credentials");
        }

        //Generating the Token
        MacAlgorithm algorithm = Jwts.SIG.HS256;   ///HS256 is a hashing algorithm for JWT
        SecretKey secretKey = algorithm.key().build();

        //Add the claims : claims are pieces of information embedded within the token
        Map<String, Object> jsonForJWT = new HashMap<>();
        jsonForJWT.put("userId",user.getId());
        jsonForJWT.put("roles",user.getRoles());
        jsonForJWT.put("createdTime",new Date());
        jsonForJWT.put("expirationTime",new Date(LocalDate.now().plusDays(3).toEpochDay()));

        //Create the token with the help of claims, algo, key
        String token = Jwts.builder()
                .claims(jsonForJWT)
                .signWith(secretKey,algorithm)
                .compact();

        //Checking for any active sessions just to cancel it
        Optional<List<Session>> checkSession = sessionRepository.findByUserIdAndSessionStatus(user.getId(), SessionStatus.ACTIVE);
        if(!checkSession.isEmpty()){
            checkSession.get().forEach(session -> session.setSessionStatus(SessionStatus.ENDED));
        }
        log.info("Token session ended");

        //Create a new session for the user
        Session session = new Session();
        session.setSessionStatus(SessionStatus.ACTIVE);
        session.setToken(token);
        session.setUser(user);

        sessionRepository.save(session);

        //Convert user into dto
        UserDto userDto = UserMapper.userToDtoMapper(user);

        //Setting up headers and cookies
        MultiValueMapAdapter<String,String> headers = new MultiValueMapAdapter<>(new HashMap<>());
        headers.add(HttpHeaders.SET_COOKIE,"auth-token:"+token);

        return new ResponseEntity<>(userDto,headers,HttpStatus.OK).getBody();
    }

    public SessionStatus validateToken(String token, Long userId) throws UnsupportedEncodingException {
        //check for token expiration
        String[] chunks_arr = token.split("\\.");  //jwt token consists of header.payload.signature
        String encrpted_payload = chunks_arr[1];
        String decrypted_json = new String(Base64.getDecoder().decode(encrpted_payload), "UTF-8");
        log.info(decrypted_json);

        //check if session is there in the db and is in active mode
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);
        if(sessionOptional.isEmpty() || sessionOptional.get().getSessionStatus() == SessionStatus.ENDED){
            throw new InvalidTokenException("Oops! Token is Invalid");
        }
        return SessionStatus.ACTIVE;
    }

    public String logout(Long userId, String token) {

        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);
        if(sessionOptional.isEmpty() || sessionOptional.get().getSessionStatus() == SessionStatus.ENDED){
            throw new InvalidTokenException("Oops! Token is Invalid");
        }
        Session session = new Session();
        session.setSessionStatus(SessionStatus.ACTIVE);
        sessionRepository.save(session);
        return "Session Ended! Logged out";
    }

    public ResponseEntity<List<Session>> getAllSessions() {

        return new ResponseEntity<>(sessionRepository.findAll(),HttpStatus.OK);
    }

    public ResponseEntity<User> getUser(String token) {
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndSessionStatus(token, SessionStatus.ACTIVE);
        if(sessionOptional.isEmpty()){
            throw new SessionNotFoundException("Session not present for token"+token);
        }
        User user = sessionOptional.get().getUser();
        return new ResponseEntity<>(user,HttpStatus.OK);

    }
}
