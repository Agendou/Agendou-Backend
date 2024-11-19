package back.api.controller;

import back.domain.dto.request.EmpresaRequestDTO;
import back.domain.dto.response.EmpresaResponseDTO;
import back.service.service.EmpresaService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@RestController
@RequestMapping("/empresas")
@AllArgsConstructor
@Validated
public class EmpresaController {

    private final EmpresaService service;

    @Operation(summary = "Login de empresa", description = "Realiza o login de uma empresa com email e senha")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid EmpresaRequestDTO empresa) {
        System.out.println("Recebida requisição de login com email: " + empresa.getEmail());
        return service.login(empresa.getEmail(), empresa.getSenha());
    }

    @Operation(summary = "Listar empresas", description = "Lista todas as empresas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empresas listadas com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmpresaResponseDTO.class)))
    })
    @GetMapping("/listar")
    public ResponseEntity<List<EmpresaResponseDTO>> listarEmpresas() {
        return ResponseEntity.status(200).body(service.listarEmpresas());
    }

    @Operation(summary = "Cadastrar empresa", description = "Cadastra uma nova empresa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Empresa cadastrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Entrada inválida")
    })
    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarEmpresa(@RequestBody @Valid EmpresaRequestDTO empresa) {
        return service.cadastrarEmpresa(empresa);
    }

    @Operation(summary = "Atualizar empresa", description = "Atualiza uma empresa existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empresa atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Empresa não encontrada")
    })
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizarEmpresa(@RequestBody @Valid EmpresaRequestDTO empresa, @PathVariable Integer id) {
        return service.atualizarEmpresa(id, empresa);
    }

    @Operation(summary = "Deletar empresa", description = "Deleta uma empresa existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empresa deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Empresa não encontrada")
    })
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deletarEmpresa(@PathVariable Integer id) {
        return service.deletarEmpresa(id);
    }
}