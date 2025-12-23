package br.com.samuckqadev.farmproject.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import br.com.samuckqadev.farmproject.dto.duck.DuckReportDTO;
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
    private final SaleReportService saleReportService;

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

    public byte[] emitReport(LocalDateTime start, LocalDateTime end) throws Exception {
        List<SaleItem> soldItems = this.saleRepository.findAllSalesWithItems().stream()
                .<SaleItem>flatMap(sale -> sale.getItems().stream())
                .toList();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        List<DuckReportDTO> reportData = soldItems.stream()
                .flatMap(item -> {
                    Duck duck = item.getDuck();
                    Duck mother = duck.getMother();

                    DuckReportDTO duckRow = DuckReportDTO.builder()
                            .duckName(mother != null ? "   " + duck.getName() : duck.getName())                                      
                            .status("Vendido")
                            .customerName(item.getSale().getCustomer().getName())
                            .customerType(
                                    item.getSale().getCustomer().isEligibleDiscount() ? "Com Desconto" : "Sem Desconto")
                            .value("R$ " + String.format("%.2f", item.getUnitPrice()))
                            .saleDate(item.getSale().getSaleDate().format(formatter))
                            .sellerName(item.getSale().getSeller().getName())
                            .build();

                    if (mother != null) {
                        DuckReportDTO motherRow = DuckReportDTO.builder()
                                .duckName(mother.getName())
                                .status("Disponível")
                                .customerName("-")
                                .customerType("-")
                                .value("-")
                                .saleDate("-")
                                .sellerName("-")
                                .build();

                        return Stream.of(motherRow, duckRow);
                    }

                    return Stream.of(duckRow);
                })
                .toList();

        return saleReportService.exportDetailedSalesToExcel(reportData, start, end);
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

        return ducks.isEmpty() ? BaseResponse.success(ducks, "No ducks saled found.")
                : BaseResponse.success(ducks, "Ducks saled listed successfully");
    }
}