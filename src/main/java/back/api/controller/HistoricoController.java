package back.api.controller;


import back.domain.dto.request.HistoricoRequestDTO;
import back.domain.dto.response.AgendamentosPorMesDTO;
import back.domain.dto.response.HistoricoAgendamentoDetalhadoResponseDTO;
import back.domain.dto.response.HistoricoResponseDTO;
import back.domain.enums.StatusAgendamento;
import back.domain.mapper.HistoricoMapper;
import back.domain.model.HistoricoAgendamento;
import back.service.service.HistoricoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/historico")
public class HistoricoController {

    @Autowired
    private HistoricoService service;

    @Operation(summary = "Obter todo o histórico", description = "Obtém todo o histórico de agendamentos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico obtido com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao obter histórico")
    })
    @GetMapping("/listar")
    public ResponseEntity<List<HistoricoAgendamentoDetalhadoResponseDTO>> obterTodoHistorico() {
        List<HistoricoAgendamentoDetalhadoResponseDTO> historico = service.obterTodoHistorico();
        return ResponseEntity.ok(historico);
    }

    @Operation(summary = "Obter histórico por empresa", description = "Obtém o histórico de agendamentos de uma empresa específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico obtido com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao obter histórico")
    })
    @GetMapping("/empresa/listar/{empresaId}")
    public ResponseEntity<List<HistoricoAgendamentoDetalhadoResponseDTO>> obterHistoricoPorEmpresa(@PathVariable Integer empresaId) {
        List<HistoricoAgendamentoDetalhadoResponseDTO> historico = service.obterHistoricoPorEmpresa(empresaId);
        return ResponseEntity.ok(historico);
    }

    @Operation(summary = "Obter histórico por período", description = "Obtém o histórico de agendamentos em um intervalo de tempo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico obtido com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao obter histórico")
    })
    @GetMapping("/por-periodo")
    public ResponseEntity<List<HistoricoResponseDTO>> obterHistoricoPorPeriodo(
            @RequestParam("dataInicio") LocalDateTime dataInicio,
            @RequestParam("dataFim") LocalDateTime dataFim,
            @RequestParam("empresaId") Integer empresaId) {
        List<HistoricoResponseDTO> historico = service.obterHistoricoPorPeriodo(dataInicio, dataFim, empresaId);
        return ResponseEntity.ok(historico);
    }

    @Operation(summary = "Obter total de agendamentos por mês", description = "Retorna o total de agendamentos agrupados por mês para uma empresa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados obtidos com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao processar os dados")
    })
    @GetMapping("/total-por-mes/empresa/{empresaId}")
    public ResponseEntity<List<AgendamentosPorMesDTO>> obterTotalAgendamentosPorMes(
            @PathVariable Integer empresaId) {
        List<AgendamentosPorMesDTO> agendamentosPorMes = service.obterTotalAgendamentosPorMes(empresaId);
        return ResponseEntity.ok(agendamentosPorMes);
    }

    @Operation(summary = "Quantidade de cancelamentos por empresa", description = "Retorna a quantidade total de cancelamentos registrados para uma empresa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quantidade de cancelamentos retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao calcular a quantidade")
    })
    @GetMapping("/cancelados/empresa/{empresaId}")
    public ResponseEntity<Long> contarCanceladosPorEmpresa(@PathVariable Integer empresaId) {
        Long totalCancelados = service.contarCanceladosPorEmpresa(empresaId);
        return ResponseEntity.ok(totalCancelados);
    }

    @Operation(summary = "Salvar histórico", description = "Salvar histórico de agendamentos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Histórico cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao cadastrar histórico")
    })
    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarHistorico(@RequestBody @Valid HistoricoRequestDTO historicoRequest) {
        HistoricoAgendamento historico = service.salvarHistorico(historicoRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(historico);
    }

    @Operation(summary = "Obter histórico por status", description = "Obtém o histórico de agendamentos por status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico obtido com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao obter histórico")
    })
    @GetMapping("/por-status")
    public ResponseEntity<List<HistoricoResponseDTO>> obterHistoricoPorStatus(@RequestParam("status") StatusAgendamento status) {
        List<HistoricoResponseDTO> historico = service.obterHistoricoPorStatus(status);
        return ResponseEntity.ok(historico);
    }

    @Operation(summary = "Listar histórico de agendamento", description = "Recupera todos os registros de histórico de um agendamento pelo ID do agendamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico(s) encontrado(s) com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhum histórico encontrado para este agendamento")
    })
    @GetMapping("/agendamento/{idAgendamento}")
    public ResponseEntity<List<HistoricoResponseDTO>> listarHistoricoPorAgendamento(
            @PathVariable Integer idAgendamento) {
        try {
            List<HistoricoResponseDTO> response = service.listarHistoricoPorAgendamento(idAgendamento);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(List.of());
        }
    }

    @Operation(summary = "Obter usuários ativos",
            description = "Obtém a lista de usuários com 4 ou mais agendamentos ativos em 2 meses, filtrando por empresa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuários ativos obtidos com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhum usuário encontrado no período")
    })
    @GetMapping("/usuarios-ativos/{empresaId}")
    public ResponseEntity<List<String>> obterUsuariosAtivos(@PathVariable Integer empresaId) {
        List<String> usuariosAtivos = service.buscarUsuariosAtivos(empresaId);

        if (usuariosAtivos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(usuariosAtivos);
        }

        return ResponseEntity.ok(usuariosAtivos);
    }

    @GetMapping("/csv")
    public ResponseEntity<byte[]> downloadCsv(
            @RequestParam("dataInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @RequestParam("dataFim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim) {

        if (dataInicio.isAfter(dataFim)) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            byte[] csvContent = service.getHistoricoCsv(dataInicio, dataFim);

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=historico.csv")
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(csvContent);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao gerar CSV: " + e.getMessage(), e);
        }
    }
}