package br.com.samuckqadev.farmproject.exception.duck;

public class DuckNotFoundException extends RuntimeException {
    public DuckNotFoundException() {
        super("The requested duck was not found.");
    }

    public DuckNotFoundException(String message) {
        super(message);
    }
}