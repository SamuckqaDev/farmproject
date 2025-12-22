package br.com.samuckqadev.farmproject.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.samuckqadev.farmproject.enums.DuckStatusEnum;
import br.com.samuckqadev.farmproject.model.Duck;



@Repository
public interface DuckRepository extends JpaRepository<Duck, UUID> {
    
    long countByMother_IdDuck(UUID idMother);

    Optional<Duck> findByName(String name);

    List<Duck> findAllByStatus(DuckStatusEnum status);

    @Query("SELECT d FROM Duck d WHERE d.status = 'SALED'")
    List<Duck> findAllDucksByStatusSaled();
    
}