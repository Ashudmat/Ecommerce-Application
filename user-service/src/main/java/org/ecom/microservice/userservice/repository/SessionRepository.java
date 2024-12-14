package org.ecom.microservice.userservice.repository;

import org.ecom.microservice.userservice.model.Session;
import org.ecom.microservice.userservice.model.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session,Long> {

    Optional<List<Session>> findByUserIdAndSessionStatus(Long id, SessionStatus sessionStatus);

    Optional<Session> findByTokenAndUser_Id(String token, Long userId);

    Optional<Session> findByTokenAndSessionStatus(String token, SessionStatus sessionStatus);
}
