package org.ecom.microservice.userservice.repository;

import org.ecom.microservice.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
