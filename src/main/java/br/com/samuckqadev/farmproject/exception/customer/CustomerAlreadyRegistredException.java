package br.com.samuckqadev.farmproject.exception.customer;

public class CustomerAlreadyRegistredException extends RuntimeException {
    
    public CustomerAlreadyRegistredException() {
        super("Customer already registered.");
    }

    public CustomerAlreadyRegistredException(String message) {
        super(message);
    }
}