package br.com.samuckqadev.formproject.model;

import java.util.UUID;

import br.com.samuckqadev.formproject.enums.DuckStatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

@Entity
@Table(name = "tb_duck")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Duck {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idDuck;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idMother")
    private Duck mother;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Default
    private DuckStatusEnum status = DuckStatusEnum.AVAILABLE;

}