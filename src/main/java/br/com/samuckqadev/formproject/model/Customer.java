package br.com.samuckqadev.formproject.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_customer")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy =  GenerationType.UUID)
    private UUID idCostumer;

    @Column(nullable = false, length = 144)
    private String name;

    @Column(name = "elegible_discount", nullable = false)
    private boolean eligibleDiscount;
    
}
