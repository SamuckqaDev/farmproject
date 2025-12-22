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

@RestController
@RequestMapping("/api/v1/seller")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    @PostMapping
    public ResponseEntity<BaseResponse<Void>> saveSeller(@RequestBody SellerRequestDTO requestDTO) {
        var response = sellerService.saveSeller(requestDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{cpf}")
    public ResponseEntity<BaseResponse<Void>> deleteSeller(@PathVariable String cpf) {
        var response = sellerService.deleteSeller(cpf);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/ranking")
    public ResponseEntity<BaseResponse<List<SellerRankingProjectionDTO>>> getRanking(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        var response = sellerService.getSellerRanking(startDate, endDate);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}