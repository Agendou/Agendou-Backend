package back.api.controller;

import back.domain.dto.request.AvaliacaoRequestDTO;
import back.domain.dto.response.AvaliacaoResponseDTO;
import back.service.service.AvaliacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/formRatings")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

    @GetMapping
    public List<AvaliacaoResponseDTO> getFormRatings() {
        try {
            return avaliacaoService.getFormRatings();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar avaliações dos formulários: " + e.getMessage());
        }
    }

    @PostMapping
    public void submitFormRating(@RequestBody AvaliacaoRequestDTO avaliacaoRequest) {
        try {
            avaliacaoService.submitFormRating(avaliacaoRequest);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar avaliação do formulário: " + e.getMessage());
        }
    }
}
