package org.ecom.microservice.userservice.model;


import jakarta.persistence.*;
import lombok.Data;

@MappedSuperclass              //to check
@Data
public class IdBaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
