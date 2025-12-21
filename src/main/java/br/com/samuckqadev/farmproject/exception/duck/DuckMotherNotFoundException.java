package br.com.samuckqadev.farmproject.exception.duck;

public class DuckMotherNotFoundException extends RuntimeException {

    public DuckMotherNotFoundException() {
        super("Duck mother not found.");
    }

    public DuckMotherNotFoundException(String message) {
        super(message);
    }
}