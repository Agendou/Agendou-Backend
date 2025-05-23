package back.api.controller;


import back.domain.dto.request.HistoricoRequestDTO;
import back.domain.dto.response.AgendamentosPorMesDTO;
import back.domain.dto.response.HistoricoResponseDTO;
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

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.Map;

@RestController
@RequestMapping("/historico")
public class HistoricoController {

    @Autowired
    private HistoricoService service;

    @Autowired
    private HistoricoMapper mapper;

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

    @Operation(summary = "Obter histórico por período", description = "Obtém o histórico de agendamentos em um intervalo de tempo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico obtido com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao obter histórico")
    })
    @GetMapping("/por-periodo")
    public ResponseEntity<List<HistoricoResponseDTO>> obterHistoricoPorPeriodo(@RequestParam("dataInicio") LocalDateTime dataInicio, @RequestParam("dataFim") LocalDateTime dataFim) {
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

    @Operation(summary = "Listar agendamentos futuros", description = "Lista todos os agendamentos futuros a partir de uma data específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamentos obtidos com sucesso"),
            @ApiResponse(responseCode = "400", description = "Data inválida fornecida")
    })
    @PostMapping("/agendamentos-futuros")
    public ResponseEntity<List<HistoricoResponseDTO>> listarAgendamentosFuturos(@RequestBody LocalDateTime dataInicio) {
        List<HistoricoResponseDTO> agendamentosFuturos = service.listarAgendamentosFuturos(dataInicio);
        return ResponseEntity.ok(agendamentosFuturos);
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

    @Operation(summary = "Listar agendamentos passados", description = "Lista todos os agendamentos do passado desde uma data específica até o momento atual")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamentos obtidos com sucesso"),
            @ApiResponse(responseCode = "400", description = "Data inválida fornecida")
    })
    @PostMapping("/agendamentos-passados")
    public ResponseEntity<List<HistoricoResponseDTO>> listarAgendamentosPassados(@RequestBody LocalDateTime dataInicio) {
        List<HistoricoResponseDTO> agendamentosPassados = service.listarAgendamentosPassados(dataInicio);
        return ResponseEntity.ok(agendamentosPassados);
    }

    @Operation(summary = "Obter usuários ativos", description = "Obtém a lista de usuários que possuem 4 ou mais agendamentos em um intervalo de 2 meses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuários ativos obtidos com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhum usuário encontrado no período")
    })
    @GetMapping("/usuarios-ativos")
    public ResponseEntity<List<String>> obterUsuariosAtivos() {
        List<String> usuariosAtivos = service.buscarUsuariosAtivos();

        if (usuariosAtivos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(usuariosAtivos);
        }

        return ResponseEntity.ok(usuariosAtivos);
    }

    @Operation(summary = "Obter agendamentos do último mês", description = "Retorna o total de agendamentos e o intervalo do mês anterior")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamentos do último mês retornados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhum agendamento encontrado no último mês")
    })
    @GetMapping("/agendamentos-ultimo-mes")
    public ResponseEntity<Map<String, Object>> obterAgendamentosUltimoMes() {
        List<HistoricoResponseDTO> agendamentos = service.obterAgendamentosUltimoMes();

        if (agendamentos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "mensagem", "Nenhum agendamento encontrado no último mês",
                    "dataInicio", LocalDate.now().minusMonths(1).withDayOfMonth(1),
                    "dataFim", LocalDate.now().minusMonths(1).with(TemporalAdjusters.lastDayOfMonth())
            ));
        }

        return ResponseEntity.ok(Map.of(
                "agendamentos", agendamentos,
                "dataInicio", LocalDate.now().minusMonths(1).withDayOfMonth(1),
                "dataFim", LocalDate.now().minusMonths(1).with(TemporalAdjusters.lastDayOfMonth())
        ));
    }


    @Operation(summary = "Quantidade de cancelamentos", description = "Retorna a quantidade total de cancelamentos registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quantidade de cancelamentos retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao calcular a quantidade")
    })
    @GetMapping("/cancelados")
    public ResponseEntity<Long> contarCancelados() {
        Long totalCancelados = service.contarCancelados();
        return ResponseEntity.ok(totalCancelados);
    }


    @Operation(summary = "Obter total de agendamentos por mês", description = "Retorna o total de agendamentos agrupados por mês")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados obtidos com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao processar os dados")
    })
    @GetMapping("/total-por-mes")
    public ResponseEntity<List<AgendamentosPorMesDTO>> obterTotalAgendamentosPorMes() {
        List<AgendamentosPorMesDTO> agendamentosPorMes = service.obterTotalAgendamentosPorMes();
        return ResponseEntity.ok(agendamentosPorMes);
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