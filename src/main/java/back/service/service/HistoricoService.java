package back.service.service;

import back.domain.dto.request.HistoricoRequestDTO;
import back.domain.dto.response.AgendamentosPorMesDTO;
import back.domain.dto.response.HistoricoAgendamentoDetalhadoResponseDTO;
import back.domain.dto.response.HistoricoResponseDTO;
import back.domain.enums.StatusAgendamento;
import back.domain.mapper.HistoricoMapper;
import back.domain.model.Agendamento;
import back.domain.model.HistoricoAgendamento;
import back.domain.repository.AgendamentoRepository;
import back.domain.repository.HistoricoRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
@Transactional
public class HistoricoService {

    private final HistoricoRepository repository;
    private final HistoricoMapper mapper;
    private final AgendamentoRepository agendamentoRepository;

    private static final Logger logger = LoggerFactory.getLogger(HistoricoService.class);

    public List<HistoricoAgendamentoDetalhadoResponseDTO> obterTodoHistorico() {
        logger.info("[HistoricoService] Buscando todo o histórico de agendamentos");
        List<HistoricoAgendamento> historicos = repository.findAll();
        return historicos.stream()
                .map(mapper::toHistoricoAgendamentoDetalhadoResponseDTO)
                .collect(Collectors.toList());
    }

    public List<HistoricoAgendamentoDetalhadoResponseDTO> obterHistoricoPorEmpresa(Integer empresaId) {
        logger.info("[HistoricoService] Buscando todo o histórico de agendamentos de uma empresa com ID: {}", empresaId);
        List<HistoricoAgendamento> historicos = repository.findByEmpresaId(empresaId);
        logger.info("[HistoricoService] Quantidade de históricos encontrados: {}", historicos.size());
        return historicos.stream()
                .map(mapper::toHistoricoAgendamentoDetalhadoResponseDTO)
                .collect(Collectors.toList());
    }

    public List<HistoricoResponseDTO> listarHistoricoPorAgendamento(Integer idAgendamento) {
        logger.info("[HistoricoService] Buscando todo o histórico de agendamentos de um único agendamento com ID: {}", idAgendamento);
        Agendamento agendamento = agendamentoRepository.findById(idAgendamento)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agendamento não encontrado com ID: " + idAgendamento));

        List<HistoricoAgendamento> historicos = repository.findByAgendamentoId(idAgendamento);

        if (historicos.isEmpty()) {
            logger.info("Nenhum histórico encontrado para o agendamento com ID: {}", idAgendamento);
        }

        logger.info("[HistoricoService] Históricos encontrados no agendamento com id: {}", idAgendamento);
        logger.info("[HistoricoService] Quantidade: {}", historicos.size());

        return historicos.stream()
                .map(mapper::toHistoricoResponseDto)
                .collect(Collectors.toList());
    }

    public List<HistoricoResponseDTO> obterHistoricoPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim, Integer empresaId) {
        logger.info("[HistoricoService] Buscando todo o histórico de agendamentos no período: {} até {}", dataInicio, dataFim);

        if (dataInicio.isAfter(dataFim)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data de início não pode ser depois da data fim");
        }
        List<HistoricoAgendamento> historicoList = repository.findByEmpresaIdAndDataBetween(empresaId, dataInicio, dataFim);

        logger.info("[HistoricoService] Históricos encontrados no período: {} até {}", dataInicio, dataFim);
        return historicoList.stream()
                .map(mapper::toHistoricoResponseDto)
                .collect(Collectors.toList());
    }

    public List<HistoricoResponseDTO> obterHistoricoPorStatus(StatusAgendamento status) {
        if (status == null) {
            logger.warn("[HistoricoService] Status nulo recebido para filtro de histórico");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status não pode ser nulo");
        }
        logger.info("[HistoricoService] Buscando histórico por status: {}", status);
        List<HistoricoAgendamento> historicoList = repository.findByStatusAtual(status);
        logger.info("[HistoricoService] Quantidade de históricos encontrados com status {}: {}", status, historicoList.size());
        return historicoList.stream()
                .map(mapper::toHistoricoResponseDto)
                .collect(Collectors.toList());
    }

    public List<String> buscarUsuariosAtivos(Integer empresaId) {
        if (empresaId == null) {
            logger.warn("[HistoricoService] ID da empresa nulo recebido para busca de usuários ativos");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID da empresa não pode ser nulo");
        }
        logger.info("[HistoricoService] Buscando usuários ativos na empresa com ID: {}", empresaId);

        LocalDateTime dataInicio = LocalDateTime.now().minusMonths(2);
        LocalDateTime dataFim = LocalDateTime.now();

        logger.info("[HistoricoService] Período de busca: {} até {}", dataInicio, dataFim);

        List<String> usuariosAtivos = repository.findActiveUsers(dataInicio, dataFim, empresaId);

        logger.info("[HistoricoService] Quantidade de usuários ativos encontrados: {}", usuariosAtivos.size());
        logger.info("[HistoricoService] Usuários ativos: {}", usuariosAtivos);
        return usuariosAtivos;
    }

    public Long contarCanceladosPorEmpresa(Integer empresaId) {
        if (empresaId == null) {
            logger.warn("[HistoricoService] ID da empresa nulo recebido para busca de agendamentos cancelados");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID da empresa não pode ser nulo");
        }
        logger.info("[HistoricoService] Buscando quantidade de históricos de agendamentos cancelados: {}", empresaId);

        Long countCancelados = repository.countCanceladosPorEmpresa(empresaId, StatusAgendamento.CANCELADO);
        logger.info("[HistoricoService] Quantidade de agendamentos cancelados encontrados: {}", countCancelados);
        return countCancelados;
    }

    public List<AgendamentosPorMesDTO> obterTotalAgendamentosPorMes(Integer empresaId) {
        if (empresaId == null) {
            logger.warn("[HistoricoService] ID da empresa nulo recebido para busca de agendamentos por mês");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID da empresa não pode ser nulo");
        }
        List<Object[]> resultados = repository.totalAgendamentosPorMes(empresaId);

        if (resultados.isEmpty()) {
            logger.info("[HistoricoService] Nenhum agendamento encontrado para a empresa com ID: {}", empresaId);
            return List.of();
        }
        logger.info("[HistoricoService] Buscando total de agendamentos por mês para a empresa com ID: {}", empresaId);
        logger.info("[HistoricoService] Resultados encontrados: {}", resultados);

        return resultados.stream()
                .map(obj -> new AgendamentosPorMesDTO((String) obj[0], ((Number) obj[1]).longValue()))
                .collect(Collectors.toList());
    }

    public byte[] getHistoricoCsv(LocalDateTime dataInicio, LocalDateTime dataFim) throws IOException {

        List<HistoricoAgendamento> historicoAgendamentos = repository.findByDataBetween(dataInicio, dataFim);
        List<HistoricoResponseDTO> historicoResponseDTOS = historicoAgendamentos.stream()
                .map(servico -> {
                    try {
                        System.out.println("Data início: " + dataInicio + ", Data fim: " + dataFim);
                        System.out.println("Quantidade de registros encontrados: " + historicoAgendamentos.size());

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
            System.out.println(new String(outputStream.toByteArray(), StandardCharsets.UTF_8));
            return outputStream.toByteArray();
        }
    }

    public HistoricoAgendamento salvarHistorico(HistoricoRequestDTO historicoRequest) {
        Agendamento agendamento = agendamentoRepository.findByIdAndStatusNot(historicoRequest.getAgendamentoId(), StatusAgendamento.CANCELADO)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado ou está cancelado"));

        HistoricoAgendamento historico = new HistoricoAgendamento();
        historico.setData(LocalDateTime.now());
        historico.setStatusAnterior(historicoRequest.getStatusAnterior());
        historico.setStatusAtual(historicoRequest.getStatusAtual());
        historico.setAgendamento(agendamento);
        historico.setEmpresa(agendamento.getFkEmpresa());
        historico.setUsuario(agendamento.getFkUsuario());

        logger.info("Salvando histórico para agendamento ID {} - de {} para {}",
                agendamento.getId(), historico.getStatusAnterior(), historico.getStatusAtual());

        return repository.save(historico);
    }

    public void registrarHistorico(Agendamento agendamento, StatusAgendamento statusAnterior, StatusAgendamento statusAtual) {
        if (agendamento == null) {
            throw new IllegalArgumentException("Agendamento não pode ser nulo ao registrar histórico.");
        }

        if (statusAnterior == null || statusAtual == null) {
            throw new IllegalArgumentException("Status anterior e atual não podem ser nulos ao registrar histórico.");
        }

        if (agendamento.getFkUsuario() == null || agendamento.getFkEmpresa() == null) {
            throw new IllegalStateException("Agendamento está com FK de usuário ou empresa nula ao registrar histórico.");
        }

        HistoricoAgendamento historico = new HistoricoAgendamento();
        historico.setData(LocalDateTime.now());
        historico.setStatusAnterior(statusAnterior);
        historico.setStatusAtual(statusAtual);
        historico.setUsuario(agendamento.getFkUsuario());
        historico.setEmpresa(agendamento.getFkEmpresa());
        historico.setAgendamento(agendamento);

        repository.save(historico);

        logger.info("[HistoricoService] Registrando histórico para agendamento ID {} - de {} para {}",
                agendamento.getId(), historico.getStatusAnterior(), historico.getStatusAtual());
    }
}