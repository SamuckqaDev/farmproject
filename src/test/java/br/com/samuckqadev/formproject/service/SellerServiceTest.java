package br.com.samuckqadev.formproject.service;

import br.com.samuckqadev.farmproject.dto.seller.SellerRankingProjectionDTO;
import br.com.samuckqadev.farmproject.dto.seller.SellerRequestDTO;
import br.com.samuckqadev.farmproject.exception.seller.SellerCpfAlreadyExists;
import br.com.samuckqadev.farmproject.exception.seller.SellerHasAssociatedSalesException;
import br.com.samuckqadev.farmproject.exception.seller.SellerNotFoundException;
import br.com.samuckqadev.farmproject.model.Seller;
import br.com.samuckqadev.farmproject.repository.SaleRepository;
import br.com.samuckqadev.farmproject.repository.SellerRepository;
import br.com.samuckqadev.farmproject.response.BaseResponse;
import br.com.samuckqadev.farmproject.service.SellerService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SellerServiceTest {

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private SaleRepository saleRepository;

    @InjectMocks
    private SellerService sellerService;

    private SellerRequestDTO sellerRequestDTO;
    private Seller seller;

    @BeforeEach
    void setUp() {
        // Criamos um DTO de exemplo
        sellerRequestDTO = SellerRequestDTO.builder()
                .name("Samuel Vendedor")
                .cpf("12345678901")
                .build();
        // Criamos uma entidade de exemplo
        seller = new Seller();
        seller.setIdSeller(UUID.randomUUID());
        seller.setName("Samuel Vendedor");
        seller.setCpf("12345678901");
        seller.setRegistration("SEL-999");
    }

    @Test
    @DisplayName("Deve registar um vendedor com sucesso quando o CPF não existe")
    void shouldSaveSellerSuccessfully() {
        // GIVEN
        when(sellerRepository.findByCpf(sellerRequestDTO.cpf())).thenReturn(Optional.empty());

        // WHEN
        BaseResponse<Void> response = sellerService.saveSeller(sellerRequestDTO);

        // THEN
        assertEquals(201, response.getStatusCode());
        assertEquals("Seller registred Successfuly!", response.getMessage());
        verify(sellerRepository, times(1)).save(any(Seller.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar registar vendedor com CPF já existente")
    void shouldThrowExceptionWhenCpfAlreadyExists() {
        // GIVEN
        when(sellerRepository.findByCpf(sellerRequestDTO.cpf())).thenReturn(Optional.of(seller));

        // WHEN & THEN
        assertThrows(SellerCpfAlreadyExists.class, () -> sellerService.saveSeller(sellerRequestDTO));
        verify(sellerRepository, never()).save(any(Seller.class));
    }

    @Test
    @DisplayName("Deve remover vendedor com sucesso quando não tem vendas associadas")
    void shouldDeleteSellerSuccessfully() {
        // GIVEN
        String cpf = "12345678901";
        when(sellerRepository.findByCpf(cpf)).thenReturn(Optional.of(seller));
        when(saleRepository.existsBySellerIdSeller(seller.getIdSeller())).thenReturn(false);

        // WHEN
        BaseResponse<Void> response = sellerService.deleteSeller(cpf);

        // THEN
        assertEquals(200, response.getStatusCode());
        assertEquals("Seller removed successfuly!", response.getMessage());
        verify(sellerRepository, times(1)).delete(seller);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar remover vendedor com vendas no sistema")
    void shouldThrowExceptionWhenSellerHasSales() {
        // GIVEN
        String cpf = "12345678901";
        when(sellerRepository.findByCpf(cpf)).thenReturn(Optional.of(seller));
        when(saleRepository.existsBySellerIdSeller(seller.getIdSeller())).thenReturn(true);

        // WHEN & THEN
        assertThrows(SellerHasAssociatedSalesException.class, () -> sellerService.deleteSeller(cpf));
        verify(sellerRepository, never()).delete(any(Seller.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar remover vendedor que não existe")
    void shouldThrowExceptionWhenSellerNotFound() {
        // GIVEN
        when(sellerRepository.findByCpf(anyString())).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(SellerNotFoundException.class, () -> sellerService.deleteSeller("999"));
    }

    @Test
    @DisplayName("Deve retornar o ranking de vendedores com sucesso")
    void shouldReturnRankingSuccessfully() {
        // GIVEN
        SellerRankingProjectionDTO projection = mock(SellerRankingProjectionDTO.class);
        when(saleRepository.getSellerRanking(any(), any())).thenReturn(List.of(projection));

        // WHEN
        BaseResponse<List<SellerRankingProjectionDTO>> response = sellerService.getSellerRanking(null, null);

        // THEN
        assertEquals(200, response.getStatusCode());
        assertFalse(response.getData().isEmpty());
        verify(saleRepository, times(1)).getSellerRanking(any(), any());
    }

    @Test
    @DisplayName("Deve retornar mensagem específica quando o ranking está vazio")
    void shouldReturnMessageWhenRankingIsEmpty() {
        // GIVEN
        when(saleRepository.getSellerRanking(any(), any())).thenReturn(Collections.emptyList());

        // WHEN
        BaseResponse<List<SellerRankingProjectionDTO>> response = sellerService.getSellerRanking(null, null);

        // THEN
        assertEquals(200, response.getStatusCode());
        assertEquals("No sales found for the selected period.", response.getMessage());
    }
}