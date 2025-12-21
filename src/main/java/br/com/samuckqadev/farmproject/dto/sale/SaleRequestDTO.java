package br.com.samuckqadev.farmproject.dto.sale;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.List;

@Builder
public record SaleRequestDTO(
        @NotBlank(message = "Customer name is required") String customerName,

        @NotBlank(message = "Seller name is required") String sellerName,

        @NotEmpty(message = "At least one duck name must be selected for sale") List<String> duckNames) {
}
