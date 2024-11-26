package back.service.service;

import back.domain.dto.request.ServicosVinculadosRequestDTO;
import back.domain.dto.response.ServicosVinculadosResponseDTO;
import back.domain.mapper.ServicosVinculadosMapper;
import back.domain.model.ServicosVinculados;
import back.domain.repository.ServicosVinculadosRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ServicosVinculadosService {
    private ServicosVinculadosRepository repository;
    private ServicosVinculadosMapper mapper;

    public List<ServicosVinculadosResponseDTO> listarServicosVinculados() {
        List<ServicosVinculados> servicosVinculados = repository.findAll();
        return servicosVinculados.stream()
                .map(mapper::toServicosVinculadosResponseDto)
                .collect(java.util.stream.Collectors.toList());
    }

    public ResponseEntity<?> atualizarServicosVinculados(Integer id, ServicosVinculadosRequestDTO servicosVinculadosRequest) {
        Optional<ServicosVinculados> optionalServicosVinculados = repository.findById(id);

        if (optionalServicosVinculados.isEmpty()) {
            return ResponseEntity.status(404).body("Serviço não encontrado.");
        }

        ServicosVinculados servicosVinculados = optionalServicosVinculados.get();
        servicosVinculados.setFk_funcionario(servicosVinculadosRequest.getFk_funcionario());
        servicosVinculados.setFk_servico(servicosVinculadosRequest.getFk_servico());

        repository.save(servicosVinculados);

        return ResponseEntity.status(200).body(mapper.toServicosVinculadosResponseDto(servicosVinculados));
    }

    public ResponseEntity<?> deletarServicosVinculados(Integer id) {
        Optional<ServicosVinculados> servicoExistente = repository.findById(id);

        if (servicoExistente.isEmpty()) {
            return ResponseEntity.status(404).body("Serviço não encontrado.");
        }

        ServicosVinculados servicosVinculados = servicoExistente.get();
        repository.delete(servicosVinculados);

        return ResponseEntity.status(200).body(servicosVinculados);
    }

    public ResponseEntity<?> cadastrarServicosVinculados(ServicosVinculadosRequestDTO dto) {

        if (repository.existsById(dto.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Serviço ja cadastrado");
        }

        ServicosVinculados servicosVinculados = mapper.toEntity(dto);
        servicosVinculados.setFk_funcionario(dto.getFk_funcionario());
        servicosVinculados.setFk_servico(dto.getFk_servico());
        ServicosVinculados servicosVinculadosSalvo = repository.save(servicosVinculados);

        ServicosVinculadosResponseDTO responseDTO = mapper.toServicosVinculadosResponseDto(servicosVinculadosSalvo);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDTO);
    }
}
