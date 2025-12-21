package br.com.samuckqadev.farmproject.dto.duck;


import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record DuckRequestDTO(String name, @NotNull(message = "Duck should be a mother") String mother) {
}
