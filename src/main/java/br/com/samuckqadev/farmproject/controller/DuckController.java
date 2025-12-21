package br.com.samuckqadev.farmproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.samuckqadev.farmproject.dto.duck.DuckRequestDTO;
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

}
