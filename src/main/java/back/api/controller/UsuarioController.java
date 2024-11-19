package back.api.controller;

import back.domain.dto.request.UsuarioRequestDTO;
import back.domain.dto.response.UsuarioResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import back.service.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.support.SessionStatus;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@AllArgsConstructor
@Validated
public class UsuarioController {

    private final UsuarioService service;

    @Operation(summary = "Login usuário", description = "Realiza o login do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful login"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping("/login")
    public ResponseEntity<UsuarioResponseDTO> login(@RequestBody @Valid UsuarioRequestDTO usuario) {
        System.out.println("Recebida requisição de login com email: " + usuario.getEmail());
        return service.login(usuario.getEmail(), usuario.getSenha());
    }

    @Operation(summary = "Lista de usuários", description = "Lista todos os usuários")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuários listados com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDTO.class)))
    })
    @GetMapping("/listar")
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios() {
        return ResponseEntity.status(200).body(service.listarUsuarios());
    }

    @Operation(summary = "Cadastrar usuário", description = "Cadastrar um novo usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário cadastrado"),
            @ApiResponse(responseCode = "400", description = "Erro no cadastro")
    })
    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarUsuario(@RequestBody @Valid UsuarioRequestDTO usuario, SessionStatus sessionStatus) {
        return service.cadastrarUsuario(usuario);
    }

    @Operation(summary = "Atualizar usuário", description = "Atualizar um usuário existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizarUsuario(@RequestBody @Valid UsuarioRequestDTO usuario, @PathVariable Integer id) {
        return service.atualizarUsuario(id, usuario);
    }

    @Operation(summary = "Deletar usuário", description = "Deletar um usuário existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário deletado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deletarUsuario(@PathVariable Integer id) {
        return service.deletarUsuario(id);
    }
}