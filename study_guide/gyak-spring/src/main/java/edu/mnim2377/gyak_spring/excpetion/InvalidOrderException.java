package edu.mnim2377.gyak_spring.excpetion;


public class InvalidOrderException extends RuntimeException {
    public InvalidOrderException(String message) {
        super(message);
    }
}
