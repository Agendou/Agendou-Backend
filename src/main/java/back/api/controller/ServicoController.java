package back.api.controller;

import back.domain.dto.request.ServicoRequestDTO;
import back.domain.model.Servico;
import back.domain.repository.ServicoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import back.service.service.ServicoService;

import java.util.List;

@RestController
@RequestMapping("/servicos")
public class ServicoController {
    @Autowired
    private ServicoService service;
    private ServicoRepository repository;

    @PostMapping()
    public ResponseEntity<String> cadastrarServico(@RequestBody @Valid ServicoRequestDTO servico) {
        try {
            service.cadastrarServico(servico);
            return ResponseEntity.status(201).body("Serviço cadastrado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Ocorreu um erro durante o cadastro.");
        }
    }

    @GetMapping()
    public ResponseEntity<List<Servico>> listarServicos() {
        try {
            return ResponseEntity.ok(service.listarServicos());
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @PutMapping
    public ResponseEntity<String> atualizarServico(@RequestBody @Valid ServicoRequestDTO servico) {
        if (servico.getId() == null) {
            return ResponseEntity.status(400).body("O id do serviço é obrigatório.");
        } else {
            try {
                service.atualizarServico(servico);
                return ResponseEntity.status(202).body("Serviço atualizado com sucesso.");
            } catch (Exception e) {
                return ResponseEntity.status(400).body("Ocorreu um erro durante a atualização do serviço.");
            }
        }
    }


    @DeleteMapping
    public ResponseEntity<String> deletarServico(@RequestBody @Valid ServicoRequestDTO servico) {
        try{
            service.deletarServico(servico);
            return ResponseEntity.status(202).body("Serviço deletado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Ocorreu um erro durante a deleção do serviço.");
        }
    }
}
