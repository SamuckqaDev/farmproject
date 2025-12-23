package br.com.samuckqadev.farmproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.samuckqadev.farmproject.dto.customer.CustomerRequestDTO;
import br.com.samuckqadev.farmproject.response.BaseResponse;
import br.com.samuckqadev.farmproject.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/customer")
// Tag define o nome do grupo no Swagger UI
@Tag(name = "Customer", description = "Recursos para gerenciamento de clientes da fazenda")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Operation(summary = "Cadastra um novo cliente", description = "Salva um cliente no banco e valida se o nome já existe. Clientes podem ter desconto nas vendas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados"),
            @ApiResponse(responseCode = "409", description = "Cliente já cadastrado com este nome")
    })
    @PostMapping
    public ResponseEntity<BaseResponse<Void>> saveCustomer(
            @RequestBody @Valid CustomerRequestDTO customerRequestDTO) {
        var reponse = customerService.saveCustomer(customerRequestDTO);
        return ResponseEntity.status(reponse.getStatusCode()).body(reponse);
    }
}