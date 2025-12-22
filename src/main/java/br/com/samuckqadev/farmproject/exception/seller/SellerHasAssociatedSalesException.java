package br.com.samuckqadev.farmproject.exception.seller;

public class SellerHasAssociatedSalesException extends RuntimeException {
    public SellerHasAssociatedSalesException() {
        super("Cannot delete seller because there are sales associated with them.");
    }

    public SellerHasAssociatedSalesException(String message) {
        super(message);
    }
}