package br.com.samuckqadev.farmproject.controller;

import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.samuckqadev.farmproject.dto.duck.DuckRequestDTO;
import br.com.samuckqadev.farmproject.dto.duck.DuckResponseDTO;
import br.com.samuckqadev.farmproject.response.BaseResponse;
import br.com.samuckqadev.farmproject.service.DuckService;

@RestController
@RequestMapping("/api/v1/duck")
public class DuckController {

    @Autowired
    private DuckService duckService;

    @PostMapping
    public ResponseEntity<BaseResponse<Void>> saveDuck(@RequestBody DuckRequestDTO duckRequestDTO) {
        var reponse = this.duckService.saveDuck(duckRequestDTO);
        return ResponseEntity.status(reponse.getStatusCode()).body(reponse);
    }

    @GetMapping("/saled")
    public ResponseEntity<BaseResponse<List<DuckResponseDTO>>> listAllSaledDuck() {
        var response = this.duckService.findAllDuckSaled();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}
