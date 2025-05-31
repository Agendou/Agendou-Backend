package back.service.service;

import back.domain.utils.Helper;
import back.domain.dto.request.AgendamentoRequestDTO;
import back.domain.dto.response.*;
import back.domain.enums.StatusAgendamento;
import back.domain.mapper.AgendamentoMapper;
import back.domain.model.*;
import back.domain.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository repository;
    private final AgendamentoMapper mapper;
    private final HistoricoService historicoService;
    private final UsuarioRepository usuarioRepository;
    private final Helper helper;

    private static final Logger logger = LoggerFactory.getLogger(AgendamentoService.class);
    private final HistoricoRepository historicoRepository;

    public ResponseEntity<?> agendar(AgendamentoRequestDTO agendamentoRequest) {
        logger.info("[AgendamentoService] Cadastrando agendamento com request dto: {}", agendamentoRequest);

        if (repository.findByDataAndStatusNot(agendamentoRequest.getData(), StatusAgendamento.CANCELADO).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Horário já agendado");
        }

        if (agendamentoRequest.getFkUsuarioId() == null || agendamentoRequest.getFkServicoId() == null || agendamentoRequest.getFkEmpresaId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Campos obrigatórios não preenchidos.");
        }

        if (agendamentoRequest.getData().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data do agendamento não pode ser no passado.");
        }

        Usuario usuario = helper.buscarUsuario(agendamentoRequest.getFkUsuarioId());
        Servico servico = helper.buscarServico(agendamentoRequest.getFkServicoId());
        Empresa empresa = helper.buscarEmpresa(agendamentoRequest.getFkEmpresaId());

        Agendamento agendamento = mapper.toEntity(agendamentoRequest);
        agendamento.setFkUsuario(usuario);
        agendamento.setFkServico(servico);
        agendamento.setFkEmpresa(empresa);
        agendamento.setStatus(StatusAgendamento.AGENDADO);

        Agendamento agendamentoSalvo = repository.save(agendamento);

        historicoService.registrarHistorico(agendamentoSalvo, StatusAgendamento.AGENDADO, StatusAgendamento.AGENDADO);

        AgendamentoResponseDTO agendamentoResponseDTO = mapper.toAgendamentoResponseDto(agendamentoSalvo);

        if (agendamentoResponseDTO == null) {
            logger.error("[AgendamentoService] Falha ao cadastrar o Agendamento");
            return ResponseEntity.status(400).build();
        }

        logger.info("[AgendamentoService] Agendamento cadastrado com sucesso: " + agendamentoResponseDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(agendamentoResponseDTO);
    }

    public Long contarNovosClientesMesAtualPorEmpresa(Integer empresaId) {
        logger.info("[AgendamentoService] Calculando novos clientes no mês atual para empresa ID: {}", empresaId);

        LocalDateTime inicioDoMes = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime fimDoMes = inicioDoMes.plusMonths(1).minusNanos(1);

        Long totalNovosClientes = repository.countNovosClientesPorEmpresa(empresaId, inicioDoMes, fimDoMes);

        logger.info("[AgendamentoService] Total de novos clientes no mês: {}", totalNovosClientes);
        return totalNovosClientes != null ? totalNovosClientes : 0L;
    }


    public BigDecimal calcularGanhoTotalPorEmpresa(Integer empresaId) {
        logger.info("[AgendamentoService] Calculando ganho total da empresa ID: {}", empresaId);
        BigDecimal total = repository.calcularGanhoTotalPorEmpresa(
                StatusAgendamento.AGENDADO,
                StatusAgendamento.REALIZADO,
                empresaId
        );
        logger.info("[AgendamentoService] Ganho total da empresa ID: {}", total);
        return total != null ? total : BigDecimal.ZERO;
    }

    //lista os agendamentos do usuário filtrando pelo id do usuário
    public List<AgendamentoResponseDTO> listarAgendamentosPorUsuario(Integer usuarioId) {
        logger.info("[AgendamentoService] Iniciando busca de agendamentos para o usuário com ID: {}", usuarioId);
        List<Agendamento> agendamentos = repository.findByFkUsuarioIdAndStatusNot(usuarioId, StatusAgendamento.CANCELADO);
        logger.info("[AgendamentoService] Total de agendamentos encontrados para o usuário {}: {}", usuarioId, agendamentos.size());
        return agendamentos.stream()
                .map(mapper::toAgendamentoResponseDto)
                .collect(Collectors.toList());
    }

    public Map<String, Long> buscarAgendamentosPorMes(Integer empresaId) {
        logger.info("[AgendamentoService] Iniciando busca de agendamentos por mês para a empresa ID: {}...", empresaId);
        List<Object[]> resultados = repository.findAgendamentosPorMesPorEmpresa(empresaId);

        Map<String, Long> agendamentosPorMes = new HashMap<>();
        String[] meses = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};

        for (Object[] resultado : resultados) {
            int mesIndex = ((Integer) resultado[0]) - 1;
            Long total = (Long) resultado[1];
            agendamentosPorMes.put(meses[mesIndex], total);
        }
        logger.info("[AgendamentoService] Agendamentos por mês para empresa {}: {}", empresaId, agendamentosPorMes);

        return agendamentosPorMes;
    }

    public List<Map<String, Object>> buscarServicosMaisRequisitados(Integer empresaId) {
        logger.info("[AgendamentoService] Iniciando busca de total de serviços mais requisitados para empresa com id: {}", empresaId);
        List<Object[]> results = repository.findServicosMaisRequisitadosPorEmpresa(empresaId);
        List<Map<String, Object>> servicos = new ArrayList<>();

        for (Object[] result : results) {
            Map<String, Object> servicoData = new HashMap<>();
            servicoData.put("nome", result[0]);
            servicoData.put("valor", result[1]);
            servicoData.put("quantidade", result[2]);
            servicos.add(servicoData);
        }

        logger.info("[AgendamentoService] Serviços mais requisitados recuperados com sucesso: {}", servicos);
        return servicos;
    }

    //Lista todos os agendamentos ativos cadastrados (sem filtros)
    public List<AgendamentoResponseDTO> listarAgendamentos() {
        logger.info("[AgendamentoService] Iniciando busca de agendamentos...");
        List<Agendamento> agendamentos = repository.findByStatusNot(StatusAgendamento.CANCELADO);
        logger.info("[AgendamentoService] Total de agendamentos encontrados: {}", agendamentos.size());
        return agendamentos.stream()
                .map(mapper::toAgendamentoResponseDto)
                .collect(Collectors.toList());
    }

    //Lista todos os agendamentos ativos filtrados pelo id da empresa
    public List<AgendamentoResponseDTO> listarAgendamentosPorEmpresa(Integer id) {
        logger.info("[AgendamentoService] Iniciando busca de agendamentos para a empresa com ID: {}", id);
        List<Agendamento> agendamentos = repository.findByFkEmpresaIdAndStatusNot(id, StatusAgendamento.CANCELADO);
        logger.info("[AgendamentoService] Total de agendamentos encontrados para a empresa {}: {}", id, agendamentos.size());
        return agendamentos.stream()
                .map(mapper::toAgendamentoResponseDto)
                .collect(Collectors.toList());
    }

    public List<AgendamentoResponseDTO> listarAgendamentosPorMesAtualOuUltimo(Integer empresaId) {
        logger.info("[AgendamentoService] Iniciando busca de agendamentos do último mês ou mês atual para empresa ID: {}", empresaId);
        LocalDateTime agora = LocalDateTime.now();

        LocalDateTime inicioMesAtual = agora.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime fimMesAtual = agora.withDayOfMonth(agora.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59).withNano(999_999_999);

        List<Agendamento> agendamentos = repository.findByFkEmpresaIdAndDataBetweenAndStatusNot(
                empresaId, inicioMesAtual, fimMesAtual, StatusAgendamento.CANCELADO
        );

        if (agendamentos.isEmpty()) {
            LocalDateTime inicioUltimoMes = inicioMesAtual.minusMonths(1);
            LocalDateTime fimUltimoMes = inicioMesAtual.minusDays(1).withHour(23).withMinute(59).withSecond(59).withNano(999_999_999);

            agendamentos = repository.findByFkEmpresaIdAndDataBetweenAndStatusNot(
                    empresaId, inicioUltimoMes, fimUltimoMes, StatusAgendamento.CANCELADO
            );
        }
        logger.info("[AgendamentoService] Total de agendamentos encontrados: {}", agendamentos.size());

        return agendamentos.stream()
                .map(mapper::toAgendamentoResponseDto)
                .collect(Collectors.toList());
    }

    public List<Object[]> buscarHorariosPico(Integer empresaId) {
        logger.info("[AgendamentoService] Iniciando busca de total de horários mais requisitados...");
        List<Object[]> horariosPico = repository.findHorariosPicoPorEmpresa(empresaId);
        logger.info("[AgendamentoService] Horários mais requisitados/horários de pico recuperados com sucesso: {}", horariosPico);
        return horariosPico;
    }

    public ResponseEntity<?> buscarAgendamentoPorId(Integer id) {
        logger.info("[AgendamentoService] Buscando agendamento com id: {}", id);
        Optional<Agendamento> agendamentoOpcional = repository.findByIdAndStatusNot(id, StatusAgendamento.CANCELADO);

        if (agendamentoOpcional.isEmpty()) {
            logger.error("Agendamento não encontrado ou cancelado com id" + id);
            return ResponseEntity.status(404).body("Agendamento não encontrado ou foi cancelado.");
        }

        Agendamento agendamento = agendamentoOpcional.get();
        AgendamentoResponseDTO responseDTO = mapper.toAgendamentoResponseDto(agendamento);

        logger.info("[AgendamentoService] Buscando agendamento com id: {}", id);

        return ResponseEntity.status(200).body(responseDTO);
    }

    @Transactional
    public ResponseEntity<?> atualizarAgendamento(Integer id, AgendamentoRequestDTO agendamentoRequest) {
        logger.info("[AgendamentoService] Atualizando agendamento com id: {}", id);
        Optional<Agendamento> agendamentoExistente = repository.findByIdAndStatusNot(id, StatusAgendamento.CANCELADO);

        if (agendamentoExistente.isEmpty()) {
            logger.error("Falha ao atualizar o agendamento");
            return ResponseEntity.status(404).body("Agendamento não encontrado.");
        }

        if (agendamentoRequest.getFkUsuarioId() == null ||
                agendamentoRequest.getFkServicoId() == null ||
                agendamentoRequest.getFkEmpresaId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Campos obrigatórios não preenchidos.");
        }

        if (agendamentoRequest.getData().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data do agendamento não pode ser no passado.");
        }

        Agendamento agendamento = agendamentoExistente.get();

        boolean houveMudanca =
                !agendamento.getData().equals(agendamentoRequest.getData()) ||
                        !agendamento.getFkServico().getId().equals(agendamentoRequest.getFkServicoId()) ||
                        !agendamento.getFkUsuario().getId().equals(agendamentoRequest.getFkUsuarioId()) ||
                        !agendamento.getFkEmpresa().getId().equals(agendamentoRequest.getFkEmpresaId());

        if (!houveMudanca) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Nenhuma alteração detectada.");
        }

        Usuario usuario = helper.buscarUsuario(agendamentoRequest.getFkUsuarioId());
        Servico servico = helper.buscarServico(agendamentoRequest.getFkServicoId());
        Empresa empresa = helper.buscarEmpresa(agendamentoRequest.getFkEmpresaId());

        StatusAgendamento statusAnterior = agendamento.getStatus();

        agendamento.setData(agendamentoRequest.getData());
        agendamento.setFkUsuario(usuario);
        agendamento.setFkServico(servico);
        agendamento.setFkEmpresa(empresa);

        // Primeiro histórico: registrar que o status foi alterado, para manter rastreabilidade da mudança
        historicoService.registrarHistorico(agendamento, statusAnterior, StatusAgendamento.ALTERADO);
        // Segundo histórico: voltar para AGENDADO (status final não muda, mas permite rastrear que houve edição)
        historicoService.registrarHistorico(agendamento, StatusAgendamento.ALTERADO, StatusAgendamento.AGENDADO);

        AgendamentoResponseDTO responseDTO = mapper.toAgendamentoResponseDto(agendamento);

        if (responseDTO == null) {
            logger.error("Falha ao atualizar o Agendamento");
            return ResponseEntity.status(400).build();
        }

        logger.info("Agendamento atualizado com sucesso: " + responseDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDTO);
    }

    //metodo softDelete - persiste os dados em um histórico e altera apenas o seu status para "cancelar" o agendamento.
    //não deleta o agendamento de fato da tabela agendamento
    @Transactional
    public ResponseEntity<?> removerAgendamento(Integer id) {
        logger.info("[AgendamentoService] Deletando agendamento com id: {}", id);
        Optional<Agendamento> agendamentoExistente = repository.findByIdAndStatusNot(id, StatusAgendamento.CANCELADO);

        if (agendamentoExistente.isEmpty()) {
            logger.error("Falha ao deletar o agendamento");
            return ResponseEntity.status(404).body("Agendamento não encontrado.");
        }

        Agendamento agendamento = agendamentoExistente.get();
        StatusAgendamento statusAnterior = agendamento.getStatus();

        Usuario usuario = helper.buscarUsuario(agendamento.getFkUsuario().getId());
        Empresa empresa = helper.buscarEmpresa(agendamento.getFkEmpresa().getId());

        agendamento.setStatus(StatusAgendamento.CANCELADO);
        repository.save(agendamento);

        historicoService.registrarHistorico(agendamento, statusAnterior, StatusAgendamento.CANCELADO);

        logger.info("Agendamento deletado com sucesso: " + agendamento.getData());
        return ResponseEntity.status(200).body("Agendamento deletado e armazenado no histórico.");
    }

    @Transactional
    public ResponseEntity<?> confirmarAtendimento(Integer id) {
        logger.info("[AgendamentoService] Confirmando atendimento para agendamento ID: {}", id);

        Optional<Agendamento> agendamentoOptional = repository.findByIdAndStatus(id, StatusAgendamento.AGENDADO);

        if (agendamentoOptional.isEmpty()) {
            logger.warn("[AgendamentoService] Agendamento não encontrado ou foi cancelado: ID {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Agendamento não encontrado ou foi cancelado.");
        }

        Agendamento agendamento = agendamentoOptional.get();

        StatusAgendamento statusAnterior = agendamento.getStatus();
        agendamento.setStatus(StatusAgendamento.REALIZADO);
        repository.save(agendamento);

        historicoService.registrarHistorico(agendamento, statusAnterior, StatusAgendamento.REALIZADO);

        logger.info("[AgendamentoService] Atendimento confirmado com sucesso para agendamento ID: {}", id);

        return ResponseEntity.ok("Atendimento confirmado com sucesso.");
    }
}
