package org.ecom.microservice.userservice.repository;

import org.ecom.microservice.userservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {

    List<Role> findAllByIdIn(List<Long> roleIds);
}
