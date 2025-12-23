package br.com.samuckqadev.formproject.service;

import br.com.samuckqadev.farmproject.dto.sale.SaleRequestDTO;
import br.com.samuckqadev.farmproject.enums.DuckStatusEnum;
import br.com.samuckqadev.farmproject.exception.duck.DuckAlreadySaledException;
import br.com.samuckqadev.farmproject.exception.duck.DuckNotFoundException;
import br.com.samuckqadev.farmproject.model.*;
import br.com.samuckqadev.farmproject.repository.CustomerRepository;
import br.com.samuckqadev.farmproject.repository.DuckRepository;
import br.com.samuckqadev.farmproject.repository.SaleRepository;
import br.com.samuckqadev.farmproject.repository.SellerRepository;
import br.com.samuckqadev.farmproject.response.BaseResponse;
import br.com.samuckqadev.farmproject.service.SaleService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaleServiceTest {

    @Mock
    private SaleRepository saleRepository;
    @Mock
    private DuckRepository duckRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private SellerRepository sellerRepository;

    @InjectMocks
    private SaleService saleService;

    private Customer customer;
    private Seller seller;
    private Duck duck;
    private SaleRequestDTO saleRequestDTO;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setName("Samuel");
        customer.setEligibleDiscount(true); // 20% de desconto

        seller = new Seller();
        seller.setName("Vendedor 1");

        duck = Duck.builder()
                .idDuck(UUID.randomUUID())
                .name("Pato 01")
                .status(DuckStatusEnum.AVAILABLE)
                .build();

        saleRequestDTO = SaleRequestDTO.builder()
                .customerName("Samuel")
                .sellerName("Vendedor 1")
                .duckNames(List.of("Pato 01"))
                .build();
    }

    @Test
    @DisplayName("Deve realizar uma venda com sucesso aplicando desconto de 20%")
    void shouldSaveSaleWithDiscountSuccessfully() {
        // GIVEN
        when(customerRepository.findByName("Samuel")).thenReturn(Optional.of(customer));
        when(sellerRepository.findByName("Vendedor 1")).thenReturn(Optional.of(seller));
        when(duckRepository.findByName("Pato 01")).thenReturn(Optional.of(duck));
        // Simula que o pato não tem filhos (preço base: 70.00)
        when(duckRepository.countByMother_IdDuck(duck.getIdDuck())).thenReturn(0L);

        ArgumentCaptor<Sale> saleCaptor = ArgumentCaptor.forClass(Sale.class);

        // WHEN
        BaseResponse<Void> response = saleService.saveSale(saleRequestDTO);

        // THEN
        assertEquals(201, response.getStatusCode());
        verify(saleRepository).save(saleCaptor.capture());
        
        Sale savedSale = saleCaptor.getValue();
        // Cálculo: 70.00 - 20% (14.00) = 56.00
        assertEquals(new BigDecimal("56.0000"), savedSale.getTotalValue()); 
        assertEquals(DuckStatusEnum.SALED, duck.getStatus());
    }

    @Test
    @DisplayName("Deve calcular preço de 50.00 quando o pato tem exatamente 1 filho")
    void shouldCalculatePrice50WhenDuckHasOneChild() {
        // GIVEN
        customer.setEligibleDiscount(false); // Sem desconto para facilitar o cálculo
        when(customerRepository.findByName(any())).thenReturn(Optional.of(customer));
        when(sellerRepository.findByName(any())).thenReturn(Optional.of(seller));
        when(duckRepository.findByName(any())).thenReturn(Optional.of(duck));
        when(duckRepository.countByMother_IdDuck(duck.getIdDuck())).thenReturn(1L);

        ArgumentCaptor<Sale> saleCaptor = ArgumentCaptor.forClass(Sale.class);

        // WHEN
        saleService.saveSale(saleRequestDTO);

        // THEN
        verify(saleRepository).save(saleCaptor.capture());
        assertEquals(new BigDecimal("50.00"), saleCaptor.getValue().getTotalValue());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o pato já estiver vendido")
    void shouldThrowExceptionWhenDuckAlreadySaled() {
        // GIVEN
        duck.setStatus(DuckStatusEnum.SALED);
        when(customerRepository.findByName(any())).thenReturn(Optional.of(customer));
        when(sellerRepository.findByName(any())).thenReturn(Optional.of(seller));
        when(duckRepository.findByName(any())).thenReturn(Optional.of(duck));

        // WHEN & THEN
        assertThrows(DuckAlreadySaledException.class, () -> saleService.saveSale(saleRequestDTO));
        verify(saleRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o pato não for encontrado")
    void shouldThrowExceptionWhenDuckNotFound() {
        // GIVEN
        when(customerRepository.findByName(any())).thenReturn(Optional.of(customer));
        when(sellerRepository.findByName(any())).thenReturn(Optional.of(seller));
        when(duckRepository.findByName("Pato 01")).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(DuckNotFoundException.class, () -> saleService.saveSale(saleRequestDTO));
    }
}