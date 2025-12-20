package br.com.samuckqadev.farmproject.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.samuckqadev.farmproject.enums.DuckStatusEnum;
import br.com.samuckqadev.farmproject.model.Duck;



@Repository
public interface DuckRepository extends JpaRepository<Duck, UUID> {
    long countByMother_IdDuck(UUID idMother);

    List<Duck> findAllByStatus(DuckStatusEnum status);
}