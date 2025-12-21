package br.com.samuckqadev.farmproject.exception.duck;

public class DuckAlreadyExistsException extends RuntimeException {

    public DuckAlreadyExistsException() {
        super("This duck is already registered in our system.");
    }

    public DuckAlreadyExistsException(String message) {
        super(message);
    }
}