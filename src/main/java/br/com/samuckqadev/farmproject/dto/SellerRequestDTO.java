package br.com.samuckqadev.farmproject.dto;

import org.hibernate.validator.constraints.br.CPF;

import lombok.Builder;

@Builder
public record SellerRequestDTO(String name, @CPF String cpf) {
}
