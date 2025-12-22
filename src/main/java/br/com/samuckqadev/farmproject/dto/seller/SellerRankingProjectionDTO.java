package br.com.samuckqadev.farmproject.dto.seller;

import java.math.BigDecimal;

public interface SellerRankingProjectionDTO {
    String getSellerName();
    Long getTotalSales();
    BigDecimal getTotalValue();
}