package back.api.controller;

import back.domain.dto.request.AgendamentoRequestDTO;

import back.domain.dto.response.AgendamentoResponseDTO;
import back.domain.model.Agendamento;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import back.service.service.AgendamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {
    @Autowired
    private AgendamentoService service;

    @Operation(summary = "Marcar horário", description = "Marca um novo horário de agendamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Horário marcado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Entrada inválida")
    })
    @PostMapping("/cadastrar")
    public ResponseEntity<?> agendar(@RequestBody @Valid AgendamentoRequestDTO agendamento) {
        System.out.println("Recebida requisição de agendamento com data e hora: " + agendamento.getData() + agendamento.hashCode());
        return service.agendar(agendamento);
    }

    @Operation(summary = "Atualizar agendamento", description = "Atualiza um agendamento existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamento atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado")
    })
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizarAgendamento(@PathVariable Integer id, @RequestBody @Valid AgendamentoRequestDTO agendamento) {
        return service.atualizarAgendamento(id,agendamento);
    }

    @Operation(summary = "Remover agendamento", description = "Remove um agendamento existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamento removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado")
    })
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> removerAgendamento(@PathVariable Integer id) {
        return service.removerAgendamento(id);
    }

    @Operation(
            summary = "Listar agendamentos cadastrados ativos",
            description = "Lista todos os agendamentos com status ativo cadastrados no sistema, sem filtro.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamentos listados com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Agendamento.class))),
            @ApiResponse(responseCode = "400", description = "Erro ao listar agendamentos")
    })
    @GetMapping("/listar")
    public ResponseEntity<List<AgendamentoResponseDTO>> listarAgendamentos() {
        return ResponseEntity.status(200).body(service.listarAgendamentos());
    }

    @Operation(summary = "Listar agendamentos cadastrados ativos filtrados por empresa", description = "Lista todos os agendamentos com status ativo cadastrados no sistema e filtrados por empresa.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamentos listados com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Agendamento.class))),
            @ApiResponse(responseCode = "400", description = "Erro ao listar agendamentos")
    })
    @GetMapping("/empresa/{id}")
    public ResponseEntity<List<AgendamentoResponseDTO>> listarAgendamentosPorEmpresa(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(service.listarAgendamentosPorEmpresa(id));
    }

    @Operation(summary = "Obter quantidade de agendamentos por mês", description = "Retorna a quantidade de agendamentos por mês")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados retornados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro na requisição")
    })
    @GetMapping("/empresa/agendamentos-por-mes/{empresaId}")
    public Map<String, Long> getAgendamentosPorMesPorEmpresa(@PathVariable Integer empresaId) {
        return service.buscarAgendamentosPorMes(empresaId);
    }

    @Operation(summary = "Listar agendamento pelo id", description = "Lista as informações do agendamento pelo id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamento encontrado.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Agendamento.class))),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado.")
    })
    @GetMapping("/listar/{id}")
    public ResponseEntity<?> getAgendamentoById(@PathVariable Integer id) {
        return service.buscarAgendamentoPorId(id);
    }

    @Operation(summary = "Listar agendamentos de uma empresa no último mês ou atual", description = "Lista agendamentos de uma empresa no último mês/mês atual..")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamentos recuperados.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Agendamento.class))),
            @ApiResponse(responseCode = "400", description = "Erro na requisição")
    })
    @GetMapping("/empresa/mes-atual-ou-ultimo/{empresaId}")
    public ResponseEntity<List<AgendamentoResponseDTO>> listarAgendamentosPorMesAtualOuUltimo(@PathVariable Integer empresaId) {
        List<AgendamentoResponseDTO> agendamentos = service.listarAgendamentosPorMesAtualOuUltimo(empresaId);
        return ResponseEntity.ok(agendamentos);
    }

    @Operation(summary = "Listar os serviços mais requisitados filtrados por uma empresa.", description = "Lista os serviços mais requisitados de uma empresa específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Serviços mais requisitados recuperados.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Agendamento.class))),
            @ApiResponse(responseCode = "400", description = "Erro na requisição")
    })
    @GetMapping("/empresa/servicos-mais-requisitados/{empresaId}")
    public ResponseEntity<List<Map<String, Object>>> getServicosMaisRequisitados(@PathVariable Integer empresaId) {
        List<Map<String, Object>> servicos = service.buscarServicosMaisRequisitados(empresaId);
        return ResponseEntity.ok(servicos);
    }

    @Operation(summary = "Listar os horários de pico de agendamento em uma empresa.", description = "Listar os horários de pico de agendamento em uma empresa específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Horários de pico recuperados.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Agendamento.class))),
            @ApiResponse(responseCode = "400", description = "Erro na requisição")
    })
    @GetMapping("/empresa/horarios-pico/{empresaId}")
    public List<Object[]> getHorariosPico(@PathVariable Integer empresaId) {
        return service.buscarHorariosPico(empresaId);
    }

    @Operation(summary = "Listar agendamentos por usuário", description = "Lista todos os agendamentos ativos (não cancelados) de um usuário específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamentos filtrados pelo usuário com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AgendamentoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Erro ao listar agendamentos")
    })
    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<AgendamentoResponseDTO>> listarAgendamentosPorUsuario(@PathVariable Integer id) {
        return ResponseEntity.ok(service.listarAgendamentosPorUsuario(id));
    }

    @Operation(summary = "Listar os ganhos previstos dos serviços agendados/realizados em uma empresa", description = "Lista o valor total dos ganhos previstos dos serviços agendados/realizados de uma empresa específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ganhos previstos recuperados com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AgendamentoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Erro ao listar os ganhos recuperados.")
    })
    @GetMapping("/empresa/ganhos/{id}")
    public ResponseEntity<BigDecimal> calcularGanhoPorEmpresa(@PathVariable Integer id) {
        return ResponseEntity.ok(service.calcularGanhoTotalPorEmpresa(id));
    }

    @Operation(summary = "Buscar os novos clientes do mês atual filtrados por empresa", description = "Exibe o total de novos clientes do mês atual filtrados por empresa.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Novos clientes recuperados com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AgendamentoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Erro ao listar novos clientes de uma empresa")
    })
    @GetMapping("/empresa/novos-clientes-mes-atual/{id}")
    public ResponseEntity<Long> getNovosClientesDoMes(@PathVariable Integer id) {
        Long total = service.contarNovosClientesMesAtualPorEmpresa(id);
        return ResponseEntity.ok(total);
    }

    @Operation(summary = "Confirmar o atendimento do agendamento, alterando seu status agendamento e registrando histórico", description = "Altera o status agendamento do agendamento para REALIZADO e registra o histórico do agendamento.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamento confirmado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AgendamentoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Erro ao confirmar agendamento"),
    })
    @PutMapping("/confirmar/{id}")
    public ResponseEntity<?> confirmarAtendimento(@PathVariable Integer id) {
        return service.confirmarAtendimento(id);
    }
}