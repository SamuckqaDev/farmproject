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
@Table(name = "tb_seller")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idSeller;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true, length = 14)
    private String cpf; 

    @Column(nullable = false, unique = true, length = 50)
    private String registration;
}
