package back.api.controller;

import back.domain.dto.request.AgendamentoRequestDTO;

import back.domain.dto.response.AgendamentoResponseDTO;

import back.domain.model.Agendamento;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import back.service.service.AgendamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
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

    @Operation(summary = "Listar agendamentos do usuário", description = "Lista todos os agendamentos de um usuário específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamentos listados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<AgendamentoResponseDTO>> listarAgendamentosPorUsuario(@PathVariable Integer usuarioId) {
        List<AgendamentoResponseDTO> agendamentos = service.listarAgendamentosPorFuncionario(usuarioId);
        return ResponseEntity.ok(agendamentos);
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

    @Operation(summary = "Listar agendamentos", description = "Lista todos os agendamentos em um intervalo de tempo")
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

    @GetMapping("/usuarios-ativos")
    public ResponseEntity<List<Integer>> buscarUsuariosAtivos() {
        try {
            List<Integer> usuariosAtivos = service.buscarUsuariosAtivos();
            return ResponseEntity.ok(usuariosAtivos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }

    @Operation(summary = "Obter quantidade de agendamentos por mês", description = "Retorna a quantidade de agendamentos por mês")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados retornados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro na requisição")
    })
    @GetMapping("/agendamentos-por-mes")
    public Map<String, Long> getAgendamentosPorMes() {
        return service.getAgendamentosPorMes();
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

    @GetMapping("/mes-atual-ou-ultimo")
    public ResponseEntity<List<AgendamentoResponseDTO>> listarAgendamentosPorMesAtualOuUltimo() {
        List<AgendamentoResponseDTO> agendamentos = service.listarAgendamentosPorMesAtualOuUltimo();
        return ResponseEntity.ok(agendamentos);
    }

    @GetMapping("/funcionarios-mais-requisitados")
    public ResponseEntity<List<Map<String, Object>>> getFuncionariosMaisRequisitados() {
        List<Map<String, Object>> funcionarios = service.getFuncionariosMaisRequisitados();
        return ResponseEntity.ok(funcionarios);
    }

    @GetMapping("/servicos-mais-requisitados")
    public ResponseEntity<List<Map<String, Object>>> getServicosMaisRequisitados() {
        List<Map<String, Object>> servicos = service.getServicosMaisRequisitados();
        return ResponseEntity.ok(servicos);
    }

    @GetMapping("/horarios-pico")
    public List<Object[]> getHorariosPico() {
        return service.getHorariosPico();
    }

}