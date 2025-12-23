package br.com.samuckqadev.formproject.service;

import br.com.samuckqadev.farmproject.dto.duck.DuckReportDTO;
import br.com.samuckqadev.farmproject.model.*;
import br.com.samuckqadev.farmproject.repository.SaleRepository;
import br.com.samuckqadev.farmproject.service.DuckService;
import br.com.samuckqadev.farmproject.service.SaleReportService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DuckServiceTest {

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private SaleReportService saleReportService;

    @InjectMocks
    private DuckService duckService;

    private Sale sale;
    private Customer customer;
    private Seller seller;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setName("Samuel");
        customer.setEligibleDiscount(true);

        seller = new Seller();
        seller.setName("Vendedor Teste");

        sale = new Sale();
        sale.setCustomer(customer);
        sale.setSeller(seller);
        sale.setSaleDate(LocalDateTime.now());
        sale.setItems(new ArrayList<>());
    }

    @Test
    @DisplayName("Cenário 1: Pato vendido com mãe cadastrada deve gerar 2 linhas (Mãe e Filho)")
    void shouldGenerateTwoRowsWhenDuckHasMother() throws Exception {
        // GIVEN
        Duck mother = Duck.builder().idDuck(UUID.randomUUID()).name("Pata Mãe 01").build();
        Duck son = Duck.builder().idDuck(UUID.randomUUID()).name("Pato Filho 1.1").mother(mother).build();

        SaleItem item = SaleItem.builder()
                .duck(son)
                .sale(sale)
                .unitPrice(new BigDecimal("70.00"))
                .build();

        sale.getItems().add(item);
        when(saleRepository.findAllSalesWithItems()).thenReturn(List.of(sale));

        // Capturamos a lista que vai para o Excel
        ArgumentCaptor<List<DuckReportDTO>> captor = ArgumentCaptor.forClass(List.class);

        // WHEN
        duckService.emitReport(null, null);

        // THEN
        verify(saleReportService).exportDetailedSalesToExcel(captor.capture(), any(), any());
        List<DuckReportDTO> reportData = captor.getValue();

        assertEquals(2, reportData.size(), "O relatório deveria ter 2 linhas (Mãe e Filho)");
        assertEquals("Pata Mãe 01", reportData.get(0).duckName());
        assertEquals("Disponível", reportData.get(0).status());
        assertTrue(reportData.get(1).duckName().contains("Pato Filho 1.1"));
        assertEquals("Vendido", reportData.get(1).status());
    }

    @Test
    @DisplayName("Cenário 2: Pato vendido sem mãe (ele é a mãe) deve gerar apenas 1 linha")
    void shouldGenerateOneRowWhenDuckIsTheMother() throws Exception {
        // GIVEN
        Duck duck = Duck.builder().idDuck(UUID.randomUUID()).name("Pata Mãe Solo").mother(null).build();

        SaleItem item = SaleItem.builder()
                .duck(duck)
                .sale(sale)
                .unitPrice(new BigDecimal("100.00"))
                .build();

        sale.getItems().add(item);
        when(saleRepository.findAllSalesWithItems()).thenReturn(List.of(sale));

        ArgumentCaptor<List<DuckReportDTO>> captor = ArgumentCaptor.forClass(List.class);

        // WHEN
        duckService.emitReport(null, null);

        // THEN
        verify(saleReportService).exportDetailedSalesToExcel(captor.capture(), any(), any());
        List<DuckReportDTO> reportData = captor.getValue();

        assertEquals(1, reportData.size(), "O relatório deveria ter apenas 1 linha");
        assertEquals("Pata Mãe Solo", reportData.get(0).duckName());
        assertEquals("Vendido", reportData.get(0).status());
    }
}