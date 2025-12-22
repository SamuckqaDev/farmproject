package br.com.samuckqadev.farmproject.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.samuckqadev.farmproject.model.Sale;


@Repository
public interface SaleRepository extends JpaRepository<Sale, UUID> {
    boolean existsBySellerUuid(UUID idSeller);
}
