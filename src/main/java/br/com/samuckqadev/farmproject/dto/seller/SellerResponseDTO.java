package br.com.samuckqadev.farmproject.dto.seller;

import lombok.Builder;

@Builder
public record SellerResponseDTO(String name, String registration, String cpf) {
}