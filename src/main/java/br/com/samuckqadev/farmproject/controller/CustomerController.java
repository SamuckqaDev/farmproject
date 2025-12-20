package br.com.samuckqadev.farmproject.controller;

import org.hibernate.boot.internal.Abstract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.samuckqadev.farmproject.dto.customer.CustomerRequestDTO;
import br.com.samuckqadev.farmproject.response.BaseResponse;
import br.com.samuckqadev.farmproject.service.CustomerService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<BaseResponse<Void>> saveCustomer(@RequestBody 
        @Valid CustomerRequestDTO customerRequestDTO) {
        var reponse = customerService.saveCustomer(customerRequestDTO);
        return ResponseEntity.status(reponse.getStatusCode()).body(reponse);
    }

}
