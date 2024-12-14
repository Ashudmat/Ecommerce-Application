package org.ecom.microservice.userservice.service;

import org.ecom.microservice.userservice.model.Role;
import org.ecom.microservice.userservice.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role createRole(String name) {
        Role role = new Role();
        role.setRole(name);
        return roleRepository.save(role);
    }
}
