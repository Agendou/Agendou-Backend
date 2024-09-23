package backend.agendou.auth.service;

import backend.agendou.auth.dto.request.UsuarioRequestDTO;
import backend.agendou.auth.dto.response.UsuarioResponseDTO;
import backend.agendou.auth.mapper.UsuarioMapper;
import backend.agendou.auth.model.Usuario;
import backend.agendou.auth.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UsuarioService {
    private final UsuarioRepository repository;
    private final UsuarioMapper mapper;

    @Autowired
    public UsuarioService(UsuarioRepository repository, UsuarioMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public ResponseEntity<String> login(String email, String senha){
        System.out.println("Iniciando login para o email: " + email);
        try {
            Optional<Usuario> optionalUsuario = repository.findByEmail(email);
            if(optionalUsuario.isEmpty()){
                return ResponseEntity.status(401).body("Usuário não encontrado.");
            }
            Usuario usuarioEntity = optionalUsuario.get();

            if (!passwordEncoder.matches(senha, usuarioEntity.getSenha())) {
                return ResponseEntity.status(401).body("Senha incorreta.");
            }

            return ResponseEntity.ok("Login realizado com sucesso!");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Ocorreu um erro durante o login.");
        }
    }


    public ResponseEntity<String> cadastrarUsuario(UsuarioRequestDTO usuarioRequest) {
        try {
            Optional<Usuario> usuarioExistente = repository.findByEmail(usuarioRequest.getEmail());

            if (usuarioExistente.isPresent()) {
                return ResponseEntity.status(409).body("Já existe um usuário com este e-mail.");
            }

            Usuario usuario = mapper.toEntity(usuarioRequest);
            usuario.setSenha(passwordEncoder.encode(usuarioRequest.getSenha()));  // Criptografa a senha

            repository.save(usuario);

            return ResponseEntity.status(201).body("Usuário cadastrado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Ocorreu um erro durante o cadastro do usuário.");
        }
    }

    // Add the following method to the UsuarioService class

    public List<UsuarioResponseDTO> listarUsuarios() {
        List<Usuario> usuarios = repository.findAll();
        return usuarios.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }


    public ResponseEntity<String> atualizarUsuario(UsuarioRequestDTO usuarioRequest) {
        try {
            Optional<Usuario> usuarioExistente = (Optional<Usuario>) repository.findByEmail(usuarioRequest.getEmail());

            if (usuarioExistente.isEmpty()) {
                return ResponseEntity.status(404).body("Usuário não encontrado.");
            }

            Usuario usuario = usuarioExistente.get();
            usuario.setEmail(usuarioRequest.getEmail());
            usuario.setSenha(usuarioRequest.getSenha());

            repository.save(usuario);

            return ResponseEntity.status(202).body("Usuário atualizado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Ocorreu um erro durante a atualização do usuário.");
        }
    }


    public ResponseEntity<String> deletarUsuario(UsuarioRequestDTO usuarioRequest) {
        try {
            Optional<Usuario> usuarioExistente = (Optional<Usuario>) repository.findByEmail(usuarioRequest.getEmail());

            if (usuarioExistente.isEmpty()) {
                return ResponseEntity.status(404).body("Usuário não encontrado.");
            }

            Usuario usuario = usuarioExistente.get();
            repository.delete(usuario);

            return ResponseEntity.status(202).body("Usuário deletado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Ocorreu um erro durante a deleção do usuário.");
        }
    }

}
