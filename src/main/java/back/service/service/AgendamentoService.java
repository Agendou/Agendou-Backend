package back.service.service;

import back.domain.dto.request.AgendamentoRequestDTO;
import back.domain.dto.response.AgendamentoResponseDTO;
import back.domain.mapper.AgendamentoMapper;
import back.domain.model.Agendamento;
import back.domain.repository.AgendamentoRepository;
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
public class AgendamentoService {

    private final AgendamentoRepository repository;
    private final AgendamentoMapper mapper;

    public ResponseEntity<?> agendar(AgendamentoRequestDTO agendamentoRequest) {

        if (repository.findByDataHoraCorte(agendamentoRequest.getDataHoraCorte()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Horário já agendado");
        }

        Agendamento agendamento = mapper.toEntity(agendamentoRequest);
        agendamento.setDataHoraCorte(agendamentoRequest.getDataHoraCorte());
        agendamento.setProfissional(agendamentoRequest.getProfissional());
        Agendamento agendamentoSalvo = repository.save(agendamento);

        AgendamentoResponseDTO responseDTO = mapper.toAgendamentoResponseDto(agendamentoSalvo);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDTO);
    }

    public List<AgendamentoResponseDTO> listarAgendamentos() {
        List<Agendamento> agendamentos = repository.findAll();
        return agendamentos.stream()
                .map(mapper::toAgendamentoResponseDto)
                .collect(Collectors.toList());
    }

    public ResponseEntity<?> atualizarAgendamento(Integer id, AgendamentoRequestDTO agendamentoRequest) {
        Optional<Agendamento> agendamentoExistente = repository.findById(id);

        if (agendamentoExistente.isEmpty()) {
            return ResponseEntity.status(404).body("Agendamento não encontrado");
        }

        Agendamento agendamento = agendamentoExistente.get();
        agendamento.setProfissional(agendamentoRequest.getProfissional());
        agendamento.setDataHoraCorte(agendamentoRequest.getDataHoraCorte());

        repository.save(agendamento);

        return ResponseEntity.status(200).body(mapper.toAgendamentoResponseDto(agendamento));
    }

    public ResponseEntity<?> removerAgendamento(Integer id) {
        Optional<Agendamento> agendamentoExistente = repository.findById(id);

        if (agendamentoExistente.isEmpty()) {
            return ResponseEntity.status(404).body("Agendamento não encontrado.");
        }

        Agendamento agendamento = agendamentoExistente.get();
        repository.delete(agendamento);

        return ResponseEntity.status(200).body(agendamento);
    }
}
