package org.ecom.microservice.userservice.exception;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String message){
        super(message);
    }
}