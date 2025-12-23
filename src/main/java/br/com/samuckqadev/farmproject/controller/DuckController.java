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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/duck")
@Tag(name = "Duck", description = "Endpoints para gerenciamento de patos e relatórios de produção")
public class DuckController {

    @Autowired
    private DuckService duckService;

    @Operation(summary = "Registra um novo pato", description = "Cria um pato no sistema vinculado a uma mãe específica.")
    @ApiResponse(responseCode = "201", description = "Pato registrado com sucesso")
    @PostMapping
    public ResponseEntity<BaseResponse<Void>> saveDuck(@RequestBody DuckRequestDTO duckRequestDTO) {
        var reponse = this.duckService.saveDuck(duckRequestDTO);
        return ResponseEntity.status(reponse.getStatusCode()).body(reponse);
    }

    @Operation(summary = "Lista patos vendidos", description = "Retorna uma lista detalhada de todos os patos que já foram vendidos.")
    @GetMapping("/saled")
    public ResponseEntity<BaseResponse<List<DuckResponseDTO>>> listAllSaledDuck() {
        var response = this.duckService.findAllDuckSaled();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @Operation(summary = "Exporta relatório de vendas em Excel", description = "Gera um arquivo .xlsx com a hierarquia de mães e filhos vendidos no período informado.")
    @ApiResponse(responseCode = "200", description = "Arquivo Excel gerado com sucesso", content = @Content(mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
    @GetMapping("/report")
    public ResponseEntity<byte[]> emitSalesReport(
            @Parameter(description = "Data inicial (ISO: yyyy-MM-ddTHH:mm:ss)", example = "2025-01-01T00:00:00") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,

            @Parameter(description = "Data final (ISO: yyyy-MM-ddTHH:mm:ss)", example = "2025-12-31T23:59:59") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        try {
            byte[] report = this.duckService.emitReport(start, end);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio_vendas_patos.xlsx")
                    .contentType(MediaType
                            .parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(report);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}