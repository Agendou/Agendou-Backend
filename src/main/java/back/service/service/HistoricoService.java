package back.service.service;

import back.domain.dto.response.HistoricoResponseDTO;
import back.domain.dto.response.ServicoResponseDTO;
import back.domain.mapper.HistoricoMapper;
import back.domain.model.HistoricoAgendamento;
import back.domain.model.Servico;
import back.domain.repository.HistoricoRepository;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
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

    public byte[] getHistoricoCsv(LocalDateTime dataInicio, LocalDateTime dataFim) throws IOException {

        List<HistoricoAgendamento> historicoAgendamentos = repository.findByDataBetween(dataInicio, dataFim);
        List<HistoricoResponseDTO> historicoResponseDTOS = historicoAgendamentos.stream()
                .map(servico -> {
                    try {
                        return mapper.toHistoricoResponseDto(servico);
                    } catch (Exception e) {
                        throw new RuntimeException("Erro ao mapear HistoricoAgendamento para HistoricoRespondeDTO", e);
                    }
                })
                .toList();

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(outputStream)) {

            writer.write("Data;StatusAnterior;StatusAtual;Agendamento\n");

            for (HistoricoResponseDTO dto : historicoResponseDTOS) {
                writer.write(String.format("%s;%s;%s;%s\n",
                        dto.getData(),
                        dto.getStatusAnterior(),
                        dto.getStatusAtual(),
                        dto.getAgendamento() != null ? dto.getAgendamento().toString() : ""));
            }

            writer.flush();
            return outputStream.toByteArray();
        }
    }
}