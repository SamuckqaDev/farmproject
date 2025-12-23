package br.com.samuckqadev.farmproject.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    // NOVO ENDPOINT PARA EMISSÃO DO RELATÓRIO
    @GetMapping("/report")
    public ResponseEntity<byte[]> emitSalesReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        try {
            // Chama o método que gera a hierarquia Mãe/Filho que criamos
            byte[] report = this.duckService.emitReport(start, end);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio_vendas_patos.xlsx")
                    .contentType(MediaType
                            .parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(report);
        } catch (Exception e) {
            // Em caso de erro, retorna 500
            return ResponseEntity.internalServerError().build();
        }
    }
}