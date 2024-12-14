package org.ecom.microservice.userservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Session extends BaseModel {
    private String token;
    private Date expiringAt;

    @ManyToOne
    private User user;
    @Enumerated(EnumType.ORDINAL)          //to check
    private SessionStatus sessionStatus;
}
