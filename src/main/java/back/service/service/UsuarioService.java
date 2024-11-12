package back.service.service;

import back.api.config.security.TokenService;
import back.domain.dto.request.UsuarioRequestDTO;
import back.domain.dto.response.UsuarioResponseDTO;
import back.domain.mapper.UsuarioMapper;
import back.domain.model.Usuario;
import back.domain.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final UsuarioMapper mapper;
    private final UsuarioRepository usuarioRepository;
    private TokenService tokenService;
    private PasswordEncoder passwordEncoder;


    public ResponseEntity<UsuarioResponseDTO> login(String email, String senha){
        System.out.println("Iniciando login para o email: " + email);

        Optional<Usuario> optionalUsuario = usuarioRepository.findByEmail(email);
        var token = tokenService.generateToken(optionalUsuario.get());
        System.out.println("Token: " + token);

        if(optionalUsuario.isEmpty()){
            return ResponseEntity.status(401).build();
        }

        Usuario usuarioEntity = optionalUsuario.get();
        UsuarioResponseDTO usuarioResponse = mapper.toUsuarioResponseDto(usuarioEntity);

        if (!passwordEncoder.matches(senha,usuarioEntity.getPassword())) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(usuarioResponse);
    }


    public ResponseEntity<?> cadastrarUsuario(UsuarioRequestDTO dto){

        if(repository.existsByEmail(dto.getEmail())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email ja cadastrado");
        }

        String encryptedPassword = passwordEncoder.encode(dto.getSenha());

        Usuario usuario = mapper.toEntity(dto);
        usuario.setNome(dto.getNome());
        usuario.setSenha(encryptedPassword);
        usuario.setEmail(dto.getEmail());
        usuario.setTelefone(dto.getTelefone());
        usuario.setRole(dto.getRole());
        Usuario usuarioSalvo = repository.save(usuario);

        UsuarioResponseDTO responseDTO = mapper.toUsuarioResponseDto(usuarioSalvo);
        System.out.println("usuario: " + responseDTO);

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
        usuario.setNome(usuarioRequest.getNome());
        usuario.setEmail(usuarioRequest.getEmail());
        usuario.setSenha(usuarioRequest.getSenha());
        usuario.setTelefone(usuario.getTelefone());

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