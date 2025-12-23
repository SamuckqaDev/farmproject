package br.com.samuckqadev.farmproject.dto.duck;

import lombok.Builder;

@Builder
public record DuckReportDTO(
        String duckName,
        String status,
        String customerName,
        String customerType, 
        String value,        
        String saleDate,     
        String sellerName    
) {
}