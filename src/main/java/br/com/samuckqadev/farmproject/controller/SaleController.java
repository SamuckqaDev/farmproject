package br.com.samuckqadev.farmproject.controller;

import br.com.samuckqadev.farmproject.dto.sale.SaleRequestDTO;
import br.com.samuckqadev.farmproject.response.BaseResponse;
import br.com.samuckqadev.farmproject.service.SaleService;
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
public class SaleController {

    @Autowired
    private SaleService saleService;

    @PostMapping
    public ResponseEntity<BaseResponse<Void>> saveSale(@Valid @RequestBody SaleRequestDTO saleRequestDTO) {
        var response = saleService.saveSale(saleRequestDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}