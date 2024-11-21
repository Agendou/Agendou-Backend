package back.api.controller;

import back.domain.dto.request.AvaliacaoRequestDTO;
import back.domain.dto.response.AvaliacaoResponseDTO;
import back.service.service.AvaliacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

    @GetMapping
    public ResponseEntity<String> getFormRatings() {
        try {
            List<AvaliacaoResponseDTO> ratings = avaliacaoService.getFormRatings();
            return ResponseEntity.ok(ratings.toString());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao buscar avaliações dos formulários: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<String> submitFormRating(@RequestBody AvaliacaoRequestDTO avaliacaoRequest) {
        try {
            avaliacaoService.submitFormRating(avaliacaoRequest);
            return ResponseEntity.status(201).body("Avaliação enviada com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao enviar avaliação do formulário: " + e.getMessage());
        }
    }


}
