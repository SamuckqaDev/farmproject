package br.com.samuckqadev.farmproject.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.samuckqadev.farmproject.dto.duck.DuckRequestDTO;
import br.com.samuckqadev.farmproject.dto.duck.DuckResponseDTO;
import br.com.samuckqadev.farmproject.enums.DuckStatusEnum;
import br.com.samuckqadev.farmproject.exception.duck.DuckAlreadyExistsException;
import br.com.samuckqadev.farmproject.exception.duck.DuckMotherNotFoundException;
import br.com.samuckqadev.farmproject.model.Duck;
import br.com.samuckqadev.farmproject.model.SaleItem;
import br.com.samuckqadev.farmproject.repository.DuckRepository;
import br.com.samuckqadev.farmproject.repository.SaleRepository;
import br.com.samuckqadev.farmproject.response.BaseResponse;
import br.com.samuckqadev.farmproject.util.GenericMapperUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DuckService {

    private final DuckRepository duckRepository;
    private final SaleRepository saleRepository;

    @Transactional
    public BaseResponse<Void> saveDuck(DuckRequestDTO duckRequestDTO) {
        this.duckRepository.findByName(duckRequestDTO.name()).ifPresent(duck -> {
            throw new DuckAlreadyExistsException();
        });

        var mother = this.duckRepository.findByName(duckRequestDTO.mother())
                .orElseThrow(DuckMotherNotFoundException::new);
        var duckEntity = GenericMapperUtil.parseObject(duckRequestDTO, Duck.class);

        duckEntity.setMother(mother);
        duckEntity.setStatus(DuckStatusEnum.AVAILABLE);
        this.duckRepository.save(duckEntity);

        return BaseResponse.created(null, "Duck registered successfully!");
    }

    public BaseResponse<List<DuckResponseDTO>> findAllDuckSaled() {
        List<DuckResponseDTO> ducks = this.saleRepository.findAllSalesWithItems().stream()
                .<SaleItem>flatMap(sale -> sale.getItems().stream())
                .map((item) -> DuckResponseDTO.builder()
                        .duckName(item.getDuck().getName())
                        .customerName(item.getSale().getCustomer().getName())
                        .sellerName(item.getSale().getSeller().getName())
                        .saleDate(item.getSale().getSaleDate())
                        .value(item.getUnitPrice())
                        .build())
                .toList();

        if (ducks.isEmpty()) {
            return BaseResponse.success(ducks, "No ducks saled found in the system.");
        }

        return BaseResponse.success(ducks, "Ducks saled listed successfully");
    }

}
