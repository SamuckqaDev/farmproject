package br.com.samuckqadev.farmproject.exception.seller;

public class SellerCpfAlreadyExists extends RuntimeException {
    public SellerCpfAlreadyExists() {
        super("Cpf already exsits");
    }

    public SellerCpfAlreadyExists(String message) {
        super(message);
    }

}
