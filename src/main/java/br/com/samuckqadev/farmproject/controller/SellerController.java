package br.com.samuckqadev.farmproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
