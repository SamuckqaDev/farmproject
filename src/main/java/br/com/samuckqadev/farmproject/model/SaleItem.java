package br.com.samuckqadev.farmproject.model;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_sale_item")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class SaleItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idSaleItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idSale", nullable = false)
    private Sale sale;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idDuck", nullable = false, unique = true)
    private Duck duck; 
    
    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;
}