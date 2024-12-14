package org.ecom.microservice.userservice.exception;

public class SessionNotFoundException extends RuntimeException {
    public SessionNotFoundException(String message){
        super(message);
    }
}
