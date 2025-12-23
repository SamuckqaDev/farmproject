package br.com.samuckqadev.farmproject.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.samuckqadev.farmproject.dto.seller.SellerRankingProjectionDTO;
import br.com.samuckqadev.farmproject.dto.seller.SellerRequestDTO;
import br.com.samuckqadev.farmproject.response.BaseResponse;
import br.com.samuckqadev.farmproject.service.SellerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/seller")
@Tag(name = "Seller", description = "Endpoints para gestão de vendedores e performance de vendas")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    @Operation(summary = "Cadastra um novo vendedor", description = "Registra um vendedor e gera automaticamente uma matrícula única.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vendedor registrado com sucesso"),
            @ApiResponse(responseCode = "409", description = "CPF já cadastrado no sistema")
    })
    @PostMapping
    public ResponseEntity<BaseResponse<Void>> saveSeller(@RequestBody SellerRequestDTO requestDTO) {
        var response = sellerService.saveSeller(requestDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @Operation(summary = "Remove um vendedor por CPF", description = "Exclui um vendedor apenas se ele não possuir vendas associadas em seu histórico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vendedor removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Vendedor não encontrado"),
            @ApiResponse(responseCode = "400", description = "Não é possível remover vendedor com vendas vinculadas")
    })
    @DeleteMapping("/{cpf}")
    public ResponseEntity<BaseResponse<Void>> deleteSeller(@PathVariable String cpf) {
        var response = sellerService.deleteSeller(cpf);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @Operation(summary = "Obtém ranking de performance", description = "Retorna o ranking de vendas por vendedor em um período específico. Caso as datas não sejam enviadas, o sistema considera os últimos 30 dias.")
    @GetMapping("/ranking")
    public ResponseEntity<BaseResponse<List<SellerRankingProjectionDTO>>> getRanking(
            @Parameter(description = "Data inicial (yyyy-MM-ddTHH:mm:ss)", example = "2025-01-01T00:00:00") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,

            @Parameter(description = "Data final (yyyy-MM-ddTHH:mm:ss)", example = "2025-12-31T23:59:59") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        var response = sellerService.getSellerRanking(startDate, endDate);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}