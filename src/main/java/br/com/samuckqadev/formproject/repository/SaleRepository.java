package br.com.samuckqadev.formproject.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.samuckqadev.formproject.model.Sale;

@Repository
public interface SaleRepository extends JpaRepository<Sale, UUID> {
    
}
