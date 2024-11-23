package back.service.service;

import back.domain.dto.request.EmpresaRequestDTO;
import back.domain.dto.response.EmpresaResponseDTO;
import back.domain.mapper.EmpresaMapper;
import back.domain.model.Empresa;
import back.domain.repository.EmpresaRepository;
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
public class EmpresaService {


    private final EmpresaRepository repository;
    private final EmpresaMapper mapper;

    public ResponseEntity<String> login(String email, String senha){
        System.out.println("Iniciando login para o email: " + email);

        Optional<Empresa> optionalEmpresa = repository.findByEmail(email);
        if(optionalEmpresa.isEmpty()){
            return ResponseEntity.status(401).body("Empresa não encontrada.");
        }
        Empresa empresaEntity = optionalEmpresa.get();
        EmpresaResponseDTO empresaResponse = mapper.toEmpresaResponseDto(empresaEntity);

        if (!empresaResponse.getSenha().equals(senha)) {
            return ResponseEntity.status(401).body("Senha incorreta.");
        }

        return ResponseEntity.ok("Login realizado com sucesso!");
    }


    public ResponseEntity<?> cadastrarEmpresa(EmpresaRequestDTO dto){

        if(repository.existsByEmail(dto.getEmail())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email ja cadastrado");
        }

        Empresa empresa = mapper.toEntity(dto);
        empresa.setNomeEmpresa(dto.getNomeEmpresa());
        empresa.setRepresentante(dto.getRepresentante());
        empresa.setSenha(dto.getSenha());
        empresa.setEmail(dto.getEmail());
        empresa.setTelefone(dto.getTelefone());
        empresa.setCnpj(dto.getCnpj());
        Empresa empresaSalvo = repository.save(empresa);

        EmpresaResponseDTO responseDTO = mapper.toEmpresaResponseDto(empresaSalvo);
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
            return ResponseEntity.status(404).body("empresa não encontrada.");
        }

        Empresa empresa = empresaExistente.get();
        repository.delete(empresa);

        return ResponseEntity.status(200).body(empresa);
    }

}
