package org.ecom.microservice.userservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User extends BaseModel {

    private String email;
    private String password;

    @ManyToMany    //As users can have many roles
    private Set<Role> roles = new HashSet<>();
}
