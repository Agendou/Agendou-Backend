package back.api.controller;

import back.domain.dto.request.AgendamentoRequestDTO;
import back.domain.model.Agendamento;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import back.service.service.AgendamentoService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {
    @Autowired
    private AgendamentoService service;

    @PostMapping()
    public ResponseEntity<String> marcarHorario(@RequestBody @Valid AgendamentoRequestDTO agendamento) {
        return service.marcarHorario(agendamento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarAgendamento(@PathVariable Integer id, @RequestBody @Valid AgendamentoRequestDTO agendamento) {
        return service.atualizarAgendamento(id, agendamento);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> removerAgendamento(@PathVariable Integer id) {
        return service.removerAgendamento(id);
    }

    @GetMapping()
    public ResponseEntity<List<Agendamento>> listarAgendamentos(
            @RequestParam(name = "inicio") LocalDateTime inicio,
            @RequestParam(name = "fim") LocalDateTime fim) {
        return service.listarAgendamentos(inicio, fim);
    }
}
