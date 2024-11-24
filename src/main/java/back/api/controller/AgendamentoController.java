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
import java.util.List;

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
        System.out.println("Recebida requisição de agendamento com data e hora: " + agendamento.getDataHoraCorte() + agendamento.hashCode());
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
}