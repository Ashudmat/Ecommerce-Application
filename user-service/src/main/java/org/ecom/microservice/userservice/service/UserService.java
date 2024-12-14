package org.ecom.microservice.userservice.service;

import org.ecom.microservice.userservice.dto.UserDto;
import org.ecom.microservice.userservice.mapper.UserMapper;
import org.ecom.microservice.userservice.model.Role;
import org.ecom.microservice.userservice.model.User;
import org.ecom.microservice.userservice.repository.RoleRepository;
import org.ecom.microservice.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public User createUser(User user) {
        userRepository.save(user);
        return user;
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> dtos = users.stream().map(user -> UserMapper.userToDtoMapper(user)).toList(); //Convert User Entities to UserDto Objects:
        return dtos;
    }

    public UserDto getUserDetails(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()) return null;
        return UserDto.from(optionalUser.get());
    }

    public UserDto setUserRoles(Long userId, List<Long> roleIds) {
        Optional<User> optionalUser = userRepository.findById(userId);
        List<Role> roles = roleRepository.findAllByIdIn(roleIds); // Fetch the Roles from the Database:

        if (optionalUser.isEmpty()) return null;

        User user  = optionalUser.get();
        user.setRoles(Set.copyOf(roles)); //Update the User's Roles:

        User savedUser = userRepository.save(user);
        return UserDto.from(savedUser);
    }

}
