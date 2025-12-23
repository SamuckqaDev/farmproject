package br.com.samuckqadev.farmproject.dto.customer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CustomerRequestDTO(
        @NotBlank(message = "Name cannot be blank") 
        @Size(max = 144, message = "Name must not exceed 144 characters") 
        String name,
        Boolean eligibleDiscount
) {}