package back.api.controller;

import back.domain.dto.request.FuncionarioRequestDTO;
import back.domain.dto.response.FuncionarioResponseDTO;
import back.service.service.FuncionarioService;
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
@RequestMapping("/funcionarios")
@AllArgsConstructor
@Validated
public class FuncionarioController {

    private final FuncionarioService service;

    @Operation(summary = "Login de funcionário", description = "Realiza o login de um funcionário com email e senha")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String senha) {
        System.out.println("Recebida requisição de login com email: " + email);
        return service.login(email, senha);
    }

    @Operation(summary = "Listar funcionários", description = "Lista todos os funcionários")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funcionários listados com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FuncionarioResponseDTO.class)))
    })
    @GetMapping("/listar")
    public ResponseEntity<List<FuncionarioResponseDTO>> listarFuncionarios() {
        return ResponseEntity.status(200).body(service.listarFuncionarios());
    }

    @Operation(summary = "Cadastrar funcionário", description = "Cadastra um novo funcionário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Funcionário cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Entrada inválida")
    })
    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarFuncionario(@RequestBody @Valid FuncionarioRequestDTO funcionario) {
        return service.cadastrarFuncionario(funcionario);
    }

    @Operation(summary = "Atualizar funcionário", description = "Atualiza um funcionário existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funcionário atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Funcionário não encontrado")
    })
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizarFuncionario(@RequestBody @Valid FuncionarioRequestDTO funcionario, @PathVariable Integer id) {
        return service.atualizarFuncionario(id, funcionario);
    }

    @Operation(summary = "Deletar funcionário", description = "Deleta um funcionário existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funcionário deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Funcionário não encontrado")
    })
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deletarFuncionario(@PathVariable Integer id) {
        return service.deletarFuncionario(id);
    }
}