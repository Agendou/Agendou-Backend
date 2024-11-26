package back.api.controller;

import back.domain.dto.request.ServicosVinculadosRequestDTO;
import back.domain.dto.response.ServicosVinculadosResponseDTO;
import back.domain.model.ServicosVinculados;
import back.service.service.ServicosVinculadosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicosvinculados")
@AllArgsConstructor
@Validated
public class ServicosVinculadosController {
    private ServicosVinculadosService service;

    @Operation(summary = "Cadastro de serviço vinculado", description = "Cadastrar um serviço vinculado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Serviço vinculado cadastrado"),
            @ApiResponse(responseCode = "400", description = "Erro no cadastro do serviço vinculado")
    })
    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarServicosVinculado(@RequestBody @Valid ServicosVinculadosRequestDTO servicosVinculados) {
        return service.cadastrarServicosVinculados(servicosVinculados);
    }

    @Operation(summary = "Lista de serviços vinculados", description = "Lista de todos os serviços vinculados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Serviços vinculados listados com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ServicosVinculados.class))),
            @ApiResponse(responseCode = "400", description = "Erro ao listar serviços vinculados")
    })
    @GetMapping("/listar")
    public ResponseEntity<List<ServicosVinculadosResponseDTO>> listarServicosVinculados() {
        return ResponseEntity.status(200).body(service.listarServicosVinculados());
    }

    @Operation(summary = "Atualizar serviço vinculado", description = "Atualizar um serviço vinculado existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Serviço vinculado atualizado"),
            @ApiResponse(responseCode = "400", description = "Entrada inválida")
    })
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizarServico( @PathVariable Integer id, @RequestBody @Valid ServicosVinculadosRequestDTO servicosVinculados) {
        return service.atualizarServicosVinculados(id, servicosVinculados);
    }

    @Operation(summary = "Deletar serviço vinculado", description = "Deletar um serviço vinculado existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Serviço vinculado deletado"),
            @ApiResponse(responseCode = "404", description = "Serviço vinculado não encontrado")
    })
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deletarServico(@PathVariable Integer id) {
        return service.deletarServicosVinculados(id);
    }
}
