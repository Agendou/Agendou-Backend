package back.api.controller;

import back.domain.dto.request.ServicoRequestDTO;
import back.domain.dto.response.ServicoResponseDTO;
import back.domain.model.Servico;
import back.domain.repository.ServicoRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import back.service.service.ServicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/servicos")
@AllArgsConstructor
@Validated
public class ServicoController {

    private ServicoService service;

    @Operation(summary = "Cadastro de serviço", description = "Cadastrar um novo serviço")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Serviço cadastrado"),
            @ApiResponse(responseCode = "400", description = "Erro no cadastro do serviço")
    })
    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarServico(@RequestBody @Valid ServicoRequestDTO servico) {
        return service.cadastrarServico(servico);
    }

    @Operation(summary = "Lista de serviços", description = "Lista todos os serviços")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Serviços listados com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Servico.class))),
            @ApiResponse(responseCode = "400", description = "Erro ao listar serviços")
    })
    @GetMapping("/listar")
    public ResponseEntity<List<ServicoResponseDTO>> listarServicos() {
        return ResponseEntity.status(200).body(service.listarServicos());
    }

    @Operation(summary = "Atualizar serviço", description = "Atualizar um serviço existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Serviço atualizado"),
            @ApiResponse(responseCode = "400", description = "Entrada inválida")
    })
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizarServico( @PathVariable Integer id, @RequestBody @Valid ServicoRequestDTO servico) {
        return service.atualizarServico(id, servico);
    }

    @Operation(summary = "Deletar serviço", description = "Deletar um serviço existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Serviço deletado"),
            @ApiResponse(responseCode = "404", description = "Serviço não encontrado")
    })
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deletarServico(@PathVariable Integer id) {
        return service.deletarServico(id);
    }

//    @GetMapping("/csv")
//    public ResponseEntity<String> downloadCsv() {
//        try {
//            byte[] csvContent = service.getServicosCsv();
//
//            Path filePath = Paths.get("src/main/resources/servicos.csv");
//
//            Files.createDirectories(filePath.getParent());
//            Files.write(filePath, csvContent);
//
//            return ResponseEntity.ok("Arquivo CSV salvo com sucesso em: " + filePath.toString());
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.internalServerError().body("Erro ao salvar o arquivo CSV.");
//        }
//    }
}