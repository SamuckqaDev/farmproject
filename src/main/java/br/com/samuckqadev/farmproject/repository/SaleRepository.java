package br.com.samuckqadev.farmproject.repository;

import java.util.Optional;
import java.util.UUID;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.samuckqadev.farmproject.dto.seller.SellerRankingProjectionDTO;
import br.com.samuckqadev.farmproject.model.Sale;

@Repository
public interface SaleRepository extends JpaRepository<Sale, UUID> {

    boolean existsBySellerIdSeller(UUID idSeller);

    @Query("SELECT s FROM Sale s JOIN s.items i WHERE i.duck.id = :idDuck")
    Optional<Sale> findByDuckId(UUID idDuck);

    @Query("SELECT s FROM Sale s JOIN FETCH s.items i JOIN FETCH i.duck")
    List<Sale> findAllSalesWithItems();

    @Query("""
           SELECT 
               s.seller.name AS sellerName, 
               COUNT(i) AS totalSales, 
               SUM(i.unitPrice) AS totalValue
           FROM Sale s 
           JOIN s.items i
           WHERE s.saleDate BETWEEN :startDate AND :endDate
           GROUP BY s.seller.name
           ORDER BY totalValue DESC
           """)
    List<SellerRankingProjectionDTO> getSellerRanking(
            @Param("startDate") LocalDateTime startDate, 
            @Param("endDate") LocalDateTime endDate
    );

}