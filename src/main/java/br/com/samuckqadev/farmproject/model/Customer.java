package br.com.samuckqadev.farmproject.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_customer")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idCustomer;

    @Column(name = "name", nullable = false, length = 144)
    private String name;

    // Ajustado o name para bater com o "elegible_discount" do seu Liquibase
    @Column(name = "elegible_discount", nullable = false)
    private boolean eligibleDiscount;

}
