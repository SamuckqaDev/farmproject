package br.com.samuckqadev.farmproject.controller;

import br.com.samuckqadev.farmproject.dto.sale.SaleRequestDTO;
import br.com.samuckqadev.farmproject.response.BaseResponse;
import br.com.samuckqadev.farmproject.service.SaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sale")
@RequiredArgsConstructor
@Tag(name = "Sale", description = "Gerenciamento do processo de vendas da fazenda")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @Operation(summary = "Registra uma nova venda de patos", description = "Realiza a venda de um ou mais patos. Calcula o valor unitário com base nos filhos do pato e aplica 20% de desconto se o cliente for elegível.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Venda realizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos ou pato já vendido", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cliente, Vendedor ou Pato não encontrado", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping
    public ResponseEntity<BaseResponse<Void>> saveSale(@Valid @RequestBody SaleRequestDTO saleRequestDTO) {
        var response = saleService.saveSale(saleRequestDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}