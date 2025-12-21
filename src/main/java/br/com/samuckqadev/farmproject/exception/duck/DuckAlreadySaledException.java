package br.com.samuckqadev.farmproject.exception.duck;

public class DuckAlreadySaledException extends RuntimeException {
    public DuckAlreadySaledException() {
        super("This duck has already been saled and is no longer available."); 
    }

    public DuckAlreadySaledException(String message) {
        super(message);
    }
}