package br.com.samuckqadev.formproject.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.samuckqadev.formproject.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID>{
   
}
