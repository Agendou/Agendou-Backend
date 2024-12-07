package back.api.controller;

import back.domain.dto.response.HistoricoResponseDTO;
import back.service.service.HistoricoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/historico")
public class HistoricoController {

    @Autowired
    private HistoricoService service;

    @Operation(summary = "Obter histórico por período", description = "Obtém o histórico de agendamentos em um intervalo de tempo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico obtido com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao obter histórico")
    })
    @GetMapping("/por-periodo")
    public ResponseEntity<List<HistoricoResponseDTO>> obterHistoricoPorPeriodo(
            @RequestParam("dataInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @RequestParam("dataFim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim) {
        List<HistoricoResponseDTO> historico = service.obterHistoricoPorPeriodo(dataInicio, dataFim);
        return ResponseEntity.ok(historico);
    }

    @Operation(summary = "Obter histórico por status", description = "Obtém o histórico de agendamentos por status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico obtido com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao obter histórico")
    })
    @GetMapping("/por-status")
    public ResponseEntity<List<HistoricoResponseDTO>> obterHistoricoPorStatus(@RequestParam("status") String status) {
        List<HistoricoResponseDTO> historico = service.obterHistoricoPorStatus(status);
        return ResponseEntity.ok(historico);
    }

    @Operation(summary = "Obter todo o histórico", description = "Obtém todo o histórico de agendamentos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico obtido com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao obter histórico")
    })
    @GetMapping("/todos")
    public ResponseEntity<List<HistoricoResponseDTO>> obterTodoHistorico() {
        List<HistoricoResponseDTO> historico = service.obterTodoHistorico();
        return ResponseEntity.ok(historico);
    }

    @GetMapping("/csv")
    public ResponseEntity<String> downloadCsv(@RequestParam("dataInicio") LocalDateTime dataInicio, @RequestParam("dataFim") LocalDateTime dataFim) {

        if (dataInicio.isAfter(dataFim)) {
            return ResponseEntity.badRequest().body("A data de início não pode ser posterior à data de fim.");
        }

        try {
            byte[] csvContent = service.getHistoricoCsv(dataInicio, dataFim);

            Path filePath = Paths.get("src/main/resources/historico.csv");

            Files.createDirectories(filePath.getParent());
            Files.write(filePath, csvContent);

            return ResponseEntity.ok("Arquivo CSV salvo com sucesso em: " + filePath.toString());

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Erro ao salvar o arquivo CSV.");
        }
    }
}