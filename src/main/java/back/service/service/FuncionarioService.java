package back.service.service;

import back.domain.dto.request.FuncionarioRequestDTO;
import back.domain.dto.response.FuncionarioResponseDTO;
import back.domain.mapper.FuncionarioMapper;
import back.domain.model.Funcionario;
import back.domain.model.Servico;
import back.domain.repository.FuncionarioRepository;
import back.domain.repository.ServicoRepository;
import jakarta.transaction.Transactional;
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
public class FuncionarioService {

    private final FuncionarioRepository repository;
    private final FuncionarioMapper mapper;
    private final ServicoRepository servicoRepository;

    public ResponseEntity<String> login(String email, String senha){
        System.out.println("Iniciando login para o email: " + email);
        Optional<Funcionario> optionalFuncionario = repository.findByEmail(email);
        if(optionalFuncionario.isEmpty()){
            return ResponseEntity.status(401).body("Usuário não encontrado.");
        }
        Funcionario funcionarioEntity = optionalFuncionario.get();
        FuncionarioResponseDTO funcionarioResponse = mapper.toFuncionarioResponseDto(funcionarioEntity);

        if (!funcionarioResponse.getSenha().equals(senha)) {
            return ResponseEntity.status(401).body("Senha incorreta.");
        }

        return ResponseEntity.ok("Login realizado com sucesso!");
    }

    @Transactional
    public ResponseEntity<?> cadastrarFuncionario(FuncionarioRequestDTO funcionarioRequestDTO) {
        try {
            Funcionario funcionario = new Funcionario();
            funcionario.setNome(funcionarioRequestDTO.getNome());
            funcionario.setEmail(funcionarioRequestDTO.getEmail());
            funcionario.setSenha(funcionarioRequestDTO.getSenha());
            funcionario.setTelefone(funcionarioRequestDTO.getTelefone());
//            funciona.setFkEmpresa(funcionarioRequestDTO.getFkEmpresa());

//            List<Servico> servicos = servicoRepository.findAllById(funcionarioRequestDTO.getServicos());
//            funcionario.setServicos(servicos);

            repository.save(funcionario);
            return ResponseEntity.status(201).body("Funcionário cadastrado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Erro ao cadastrar funcionário: " + e.getMessage());
        }
    }

    public List<FuncionarioResponseDTO> listarFuncionarios() {
        List<Funcionario> funcionarios = repository.findAll();
        return funcionarios.stream()
                .map(mapper::toFuncionarioResponseDto)
                .collect(Collectors.toList());
    }


    public ResponseEntity<?> atualizarFuncionario(Integer id, FuncionarioRequestDTO funcionarioRequest) {
        Optional<Funcionario> funcionarioExistente = repository.findById(id);

        if (funcionarioExistente.isEmpty()) {
            return ResponseEntity.status(404).body("Usuário não encontrado.");
        }

        Funcionario funcionario = funcionarioExistente.get();
        funcionario.setNome(funcionarioRequest.getNome());
        funcionario.setEmail(funcionarioRequest.getEmail());
        funcionario.setSenha(funcionarioRequest.getSenha());
        funcionario.setTelefone(funcionarioRequest.getTelefone());

        repository.save(funcionario);

        return ResponseEntity.status(200).body(mapper.toFuncionarioResponseDto(funcionario));
    }


    public ResponseEntity<?> deletarFuncionario(Integer id) {
        Optional<Funcionario> funcionarioExistente = repository.findById(id);

        if (funcionarioExistente.isEmpty()) {
            return ResponseEntity.status(404).body("Usuário não encontrado.");
        }

        Funcionario funcionario = funcionarioExistente.get();
        repository.delete(funcionario);

        return ResponseEntity.status(200).body(funcionario);
    }
}
