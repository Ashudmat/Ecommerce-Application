package org.ecom.microservice.userservice.controller;

import org.ecom.microservice.userservice.dto.RoleDto;
import org.ecom.microservice.userservice.model.Role;
import org.ecom.microservice.userservice.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(@RequestBody RoleService roleService) {               //////////////
        this.roleService = roleService;
    }
    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody RoleDto request){
        Role role = roleService.createRole(request.getName());
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

}
