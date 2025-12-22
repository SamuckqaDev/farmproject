package br.com.samuckqadev.farmproject.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.samuckqadev.farmproject.model.Seller;


@Repository
public interface SellerRepository extends JpaRepository<Seller, UUID> {
    Optional<Seller> findByCpf(String cpf);

    Optional<Seller> findByName(String sellerName);
}
