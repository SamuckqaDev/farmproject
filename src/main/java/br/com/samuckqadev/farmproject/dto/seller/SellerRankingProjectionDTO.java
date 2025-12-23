package br.com.samuckqadev.farmproject.dto.seller;

import java.math.BigDecimal;

public record SellerRankingProjectionDTO(String getSellerName,
        Long getTotalSales,
        BigDecimal getTotalValue) {

}