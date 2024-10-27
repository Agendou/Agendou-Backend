package back.service.service;

import back.domain.dto.request.UsuarioRequestDTO;
import back.domain.dto.response.UsuarioResponseDTO;
import back.domain.mapper.UserMapper;
import back.domain.model.Usuario;
import back.domain.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UsuarioService {
    private final UsuarioRepository repository;
    private final UserMapper mapper;

    public ResponseEntity<String> login(String email, String senha){
        System.out.println("Iniciando login para o email: " + email);
        Optional<Usuario> optionalUsuario = repository.findByEmail(email);
        if(optionalUsuario.isEmpty()){
            return ResponseEntity.status(401).body("Usuário não encontrado.");
        }
        Usuario usuarioEntity = optionalUsuario.get();
        UsuarioResponseDTO usuarioResponse = mapper.toUsuarioResponseDto(usuarioEntity);

        if (!usuarioResponse.getSenha().equals(senha)) {
            return ResponseEntity.status(401).body("Senha incorreta.");
        }

        return ResponseEntity.ok("Login realizado com sucesso!");
    }


    public ResponseEntity<?> cadastrarUsuario(UsuarioRequestDTO dto){

        if(repository.existsByEmail(dto.getEmail())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email ja cadastrado");
        }

        Usuario usuario = mapper.toEntity(dto);
        usuario.setNome(dto.getNome());
        usuario.setSenha(dto.getSenha());
        usuario.setEmail(dto.getEmail());
        Usuario usuarioSalvo = repository.save(usuario);

        UsuarioResponseDTO responseDTO = mapper.toUsuarioResponseDto(usuarioSalvo);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDTO);
    }


    public List<UsuarioResponseDTO> listarUsuarios() {
        List<Usuario> usuarios = repository.findAll();
        return usuarios.stream()
                .map(mapper::toUsuarioResponseDto)
                .collect(Collectors.toList());
    }


    public ResponseEntity<?> atualizarUsuario(Integer id, UsuarioRequestDTO usuarioRequest) {
        Optional<Usuario> usuarioExistente = repository.findById(id);

        if (usuarioExistente.isEmpty()) {
            return ResponseEntity.status(404).body("Usuário não encontrado.");
        }

        Usuario usuario = usuarioExistente.get();
        usuario.setEmail(usuarioRequest.getEmail());
        usuario.setSenha(usuarioRequest.getSenha());

        repository.save(usuario);

        return ResponseEntity.status(200).body(mapper.toUsuarioResponseDto(usuario));
    }


    public ResponseEntity<?> deletarUsuario(Integer id) {
        Optional<Usuario> usuarioExistente = repository.findById(id);

        if (usuarioExistente.isEmpty()) {
            return ResponseEntity.status(404).body("Usuário não encontrado.");
        }

        Usuario usuario = usuarioExistente.get();
        repository.delete(usuario);

        return ResponseEntity.status(200).body(usuario);
    }

}