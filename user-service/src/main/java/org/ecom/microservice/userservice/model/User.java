package org.ecom.microservice.userservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User extends IdBaseModel {

    private String email;
    private String password;

    @ManyToMany    //users can have many roles
    private Set<Role> roles = new HashSet<>();
}
