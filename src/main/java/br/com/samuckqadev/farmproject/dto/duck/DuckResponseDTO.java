package br.com.samuckqadev.farmproject.dto.duck;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record DuckResponseDTO(
        String duckName,
        String customerName,
        String sellerName,
        LocalDateTime saleDate,
        BigDecimal value) {
}