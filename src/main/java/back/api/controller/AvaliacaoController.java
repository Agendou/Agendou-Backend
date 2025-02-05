package back.api.controller;

import back.domain.dto.response.AvaliacaoResponseDTO;
import back.service.service.AvaliacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

    @Operation(summary = "Enviar Avaliação", description = "Envia uma avaliação para a API JotForm")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Avaliação enviada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao enviar avaliação"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @PostMapping("/enviar")
    public ResponseEntity<?> enviarAvaliacao(@RequestBody AvaliacaoRequestDTO avaliacaoRequest) {
        return avaliacaoService.enviarAvaliacao(avaliacaoRequest);
    }

    @Operation(summary = "Listar Avaliações", description = "Lista todas as avaliações do banco de dados e da API JotForm")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avaliações listadas com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AvaliacaoResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erro ao listar avaliações")
    })
    @GetMapping("/listar")
    public ResponseEntity<List<AvaliacaoResponseDTO>> listarAvaliacoes() {
        return avaliacaoService.listarAvaliacoes();
    }

    @Operation(summary = "Calcular Média de Estrelas", description = "Calcula a média de estrelas das avaliações no banco")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Média calculada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao calcular média")
    })
    @GetMapping("/media-estrelas")
    public ResponseEntity<Integer> calcularMediaEstrelas() {
        int media = avaliacaoService.calcularMediaEstrelas();
        return ResponseEntity.ok(media);
    }

}
