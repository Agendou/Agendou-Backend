package back.service.service;

import back.domain.dto.response.HistoricoResponseDTO;
import back.domain.mapper.HistoricoMapper;
import back.domain.model.HistoricoAgendamento;
import back.domain.repository.HistoricoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoricoService {

    private final HistoricoRepository repository;
    private final HistoricoMapper mapper;

    public HistoricoService(HistoricoRepository historicoRepository, HistoricoMapper historicoMapper) {
        this.repository = historicoRepository;
        this.mapper = historicoMapper;
    }

    public List<HistoricoResponseDTO> obterHistoricoPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        List<HistoricoAgendamento> historicoList = repository.findByDataBetween(dataInicio, dataFim);
        return historicoList.stream()
                .map(mapper::toHistoricoResponseDto)
                .collect(Collectors.toList());
    }

    public List<HistoricoResponseDTO> obterHistoricoPorStatus(String status) {
        List<HistoricoAgendamento> historicoList = repository.findByStatusAtual(status);
        return historicoList.stream()
                .map(mapper::toHistoricoResponseDto)
                .collect(Collectors.toList());
    }

    public List<HistoricoResponseDTO> obterTodoHistorico() {
        List<HistoricoAgendamento> historicoList = repository.findAll();
        return historicoList.stream()
                .map(mapper::toHistoricoResponseDto)
                .collect(Collectors.toList());
    }
}