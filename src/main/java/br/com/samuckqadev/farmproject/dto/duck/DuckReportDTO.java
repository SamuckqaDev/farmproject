package br.com.samuckqadev.farmproject.dto.duck;

import lombok.Builder;

@Builder
public record DuckReportDTO(
        String duckName,
        String status,
        String customerName,
        String customerType, // Em inglês, como você pediu
        String value,        // String para já ir com "R$" ou "-"
        String saleDate,     // String já formatada
        String sellerName    // Em inglês
) {
}