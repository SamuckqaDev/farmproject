package br.com.samuckqadev.farmproject.exception.seller;

public class SellerNotFoundException extends RuntimeException {
    public SellerNotFoundException() {
        super("Seller not found. Please check the name and try again.");
    }

    public SellerNotFoundException(String message) {
        super(message);
    }
}