package back.service.service;

import back.api.config.security.TokenService;
import back.domain.dto.request.EmpresaRequestDTO;
import back.domain.dto.response.EmpresaResponseDTO;
import back.domain.dto.response.LoginResponseDTO;
import back.domain.mapper.EmpresaMapper;
import back.domain.model.Empresa;
import back.domain.repository.EmpresaRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmpresaService {


    private final EmpresaRepository repository;
    private final EmpresaMapper mapper;
    private TokenService tokenService;
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(EmpresaService.class);

    public ResponseEntity<?> login(String email, String senha){
        System.out.println("Iniciando login para o email: " + email);

        Optional<Empresa> optionalEmpresa = repository.findByEmail(email);
        var token = tokenService.generateToken(optionalEmpresa.get());
        System.out.println("Token: " + token);

        if(optionalEmpresa.isEmpty()){
            logger.error("Falha na autenticação da empresa: A Empresa está vazia ou não existe");
            return ResponseEntity.status(401).build();
        }
        Empresa empresaEntity = optionalEmpresa.get();
        EmpresaResponseDTO empresaResponse = mapper.toEmpresaResponseDto(empresaEntity);
        LoginResponseDTO loginDTO = new LoginResponseDTO(empresaResponse, token);

        if (!passwordEncoder.matches(senha,empresaEntity.getSenha())) {
            logger.error("Falha na autenticação da senha: A senha está vazia ou incorreta");
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(loginDTO);
    }


    public ResponseEntity<?> cadastrarEmpresa(EmpresaRequestDTO dto){

        if(repository.existsByEmail(dto.getEmail())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email ja cadastrado");
        }

        String encryptedPassword = passwordEncoder.encode(dto.getSenha());

        Empresa empresa = mapper.toEntity(dto);
        empresa.setNomeEmpresa(dto.getNomeEmpresa());
        empresa.setRepresentante(dto.getRepresentante());
        empresa.setSenha(encryptedPassword);
        empresa.setEmail(dto.getEmail());
        empresa.setTelefone(dto.getTelefone());
        empresa.setCnpj(dto.getCnpj());
        empresa.setRole(dto.getRole());
        Empresa empresaSalvo = repository.save(empresa);

        EmpresaResponseDTO responseDTO = mapper.toEmpresaResponseDto(empresaSalvo);

        if (responseDTO == null) {
            logger.error("Falha ao cadastrar a empresa: A empresa está vazio");
            return ResponseEntity.status(400).build();
        }

        logger.info("Empresa cadastrada com sucesso: " + responseDTO.getEmail());

        System.out.println("empresa: " + responseDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDTO);
    }


    public List<EmpresaResponseDTO> listarEmpresas() {
        List<Empresa> empresas = repository.findAll();
        return empresas.stream()
                .map(mapper::toEmpresaResponseDto)
                .collect(Collectors.toList());
    }


    public ResponseEntity<?> atualizarEmpresa(Integer id, EmpresaRequestDTO empresaRequest) {
        Optional<Empresa> empresaExistente = repository.findById(id);

        if (empresaExistente.isEmpty()) {
            logger.error("Falha ao atualizar a empresa: Empresa não encontrado");
            return ResponseEntity.status(404).body("Empresa não encontrada.");
        }

        Empresa empresa = empresaExistente.get();
        empresa.setNomeEmpresa(empresaRequest.getNomeEmpresa());
        empresa.setCnpj(empresaRequest.getCnpj());
        empresa.setRepresentante(empresaRequest.getRepresentante());
        empresa.setEmail(empresaRequest.getEmail());
        empresa.setSenha(empresaRequest.getSenha());
        empresa.setTelefone(empresaRequest.getTelefone());

        repository.save(empresa);

        return ResponseEntity.status(200).body(mapper.toEmpresaResponseDto(empresa));
    }


    public ResponseEntity<?> deletarEmpresa(Integer id) {
        Optional<Empresa> empresaExistente = repository.findById(id);

        if (empresaExistente.isEmpty()) {
            logger.error("Falha ao deletar a empresa: Empresa não encontrado");
            return ResponseEntity.status(404).body("empresa não encontrada.");
        }
        Empresa empresa = empresaExistente.get();
        logger.info("Empresa deletada com sucesso: " + empresa.getEmail());
        repository.delete(empresa);

        return ResponseEntity.status(200).body(empresa);
    }

}
