package back.service.service;

import back.api.controller.HistoricoController;
import back.domain.dto.request.AgendamentoRequestDTO;
import back.domain.dto.request.HistoricoRequestDTO;
import back.domain.dto.response.*;
import back.domain.mapper.AgendamentoMapper;
import back.domain.model.*;
import back.domain.repository.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository repository;
    private final AgendamentoMapper mapper;
    private final ServicoRepository servicoRepository;
    private final HistoricoService historicoService;
    private final UsuarioRepository usuarioRepository;

    private static final Logger logger = LoggerFactory.getLogger(AgendamentoService.class);
    private final HistoricoRepository historicoRepository;

    public ResponseEntity<?> agendar(AgendamentoRequestDTO agendamentoRequest) {

        if (repository.findByData(agendamentoRequest.getData()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Horário já agendado");
        }

        Usuario usuario = usuarioRepository.findById(agendamentoRequest.getFkUsuario())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        Agendamento agendamento = mapper.toEntity(agendamentoRequest);
        agendamento.setFkUsuario(usuario);

        Agendamento agendamentoSalvo = repository.save(agendamento);

        HistoricoAgendamento historico = new HistoricoAgendamento();
        historico.setData(agendamentoSalvo.getData());
        historico.setStatusAnterior("Criado");
        historico.setStatusAtual("Agendado");
        historico.setNomeUsuario(
                agendamentoSalvo.getFkUsuario() != null ? agendamentoSalvo.getFkUsuario().getNome() : "Usuário desconhecido"
        );
        historico.setNomeServico(
                agendamentoSalvo.getFkServico() != null ? agendamentoSalvo.getFkServico().getNome() : "Serviço não informado"
        );

        historicoRepository.save(historico);

        AgendamentoResponseDTO responseDTO = mapper.toAgendamentoResponseDto(agendamentoSalvo);

        if (responseDTO == null) {
            logger.error("Falha ao cadastrar o Agendamento");
            return ResponseEntity.status(400).build();
        }

        logger.info("Agendamento cadastrado com sucesso: " + responseDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDTO);
    }

    public List<AgendamentoResponseDTO> listarAgendamentosPorUsuario(Integer usuarioId) {
        List<Agendamento> agendamentos = repository.findAllByFkUsuarioId(usuarioId);
        return agendamentos.stream()
                .map(mapper::toAgendamentoResponseDto)
                .collect(Collectors.toList());
    }

    public Map<String, Long> getAgendamentosPorMes() {
        List<Object[]> resultados = repository.findAgendamentosPorMes();

        Map<String, Long> agendamentosPorMes = new HashMap<>();
        String[] meses = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};

        for (Object[] resultado : resultados) {
            int mesIndex = ((Integer) resultado[0]) - 1;
            Long total = (Long) resultado[1];
            agendamentosPorMes.put(meses[mesIndex], total);
        }

        return agendamentosPorMes;
    }

    public List<Map<String, Object>> getServicosMaisRequisitados() {
        List<Object[]> results = repository.findServicosMaisRequisitados();
        List<Map<String, Object>> servicos = new ArrayList<>();

        for (Object[] result : results) {
            Map<String, Object> servicoData = new HashMap<>();
            servicoData.put("nome", result[0]);
            servicoData.put("quantidade", result[1]);
            servicos.add(servicoData);
        }

        return servicos;
    }

    public List<AgendamentoResponseDTO> listarAgendamentos() {
        List<Agendamento> agendamentos = repository.findAll();
        return agendamentos.stream()
                .map(mapper::toAgendamentoResponseDto)
                .collect(Collectors.toList());
    }

    public List<AgendamentoResponseDTO> listarAgendamentosPorMesAtualOuUltimo() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime inicioMesAtual = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime fimMesAtual = now.withDayOfMonth(now.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        List<Agendamento> agendamentosDoMesAtual = repository.findByDataBetween(inicioMesAtual, fimMesAtual);

        if (agendamentosDoMesAtual.isEmpty()) {
            LocalDateTime inicioUltimoMes = inicioMesAtual.minusMonths(1);
            LocalDateTime fimUltimoMes = inicioMesAtual.minusDays(1).withHour(23).withMinute(59).withSecond(59).withNano(999999999);

            agendamentosDoMesAtual = repository.findByDataBetween(inicioUltimoMes, fimUltimoMes);
        }
        return agendamentosDoMesAtual.stream()
                .map(mapper::toAgendamentoResponseDto)
                .collect(Collectors.toList());
    }

    public List<Object[]> getHorariosPico() {
        return repository.findHorariosPico();
    }


    public ResponseEntity<?> buscarAgendamentoPorId(Integer id) {
        Optional<Agendamento> agendamentoExistente = repository.findById(id);

        if (agendamentoExistente.isEmpty()) {
            logger.error("Empresa com id " + id + " não encontrado");
            return ResponseEntity.status(404).body("Empresa não encontrado");
        }

        Agendamento agendamento = agendamentoExistente.get();
        AgendamentoResponseDTO responseDTO = mapper.toAgendamentoResponseDto(agendamento);

        return ResponseEntity.status(200).body(responseDTO);
    }


    public List<Integer> buscarUsuariosAtivos() {
        LocalDateTime dataInicio = LocalDateTime.now().minusMonths(1);
        LocalDateTime dataFim = LocalDateTime.now();

        List<Integer> usuariosAtivos = new ArrayList<>();

        List<Usuario> listaDeUsuarios = usuarioRepository.findAll();

        for (Usuario usuario : listaDeUsuarios) {
            Long agendamentos = repository.countAgendamentosPorUsuarioNoPeriodo(usuario.getId(), dataInicio, dataFim);

            if (agendamentos != null && agendamentos > 5) {
                usuariosAtivos.add(usuario.getId());
            }
        }

        return usuariosAtivos;
    }


    @Transactional
    public ResponseEntity<?> atualizarAgendamento(Integer id, AgendamentoRequestDTO agendamentoRequest) {
        Optional<Agendamento> agendamentoExistente = repository.findById(id);

        if (agendamentoExistente.isEmpty()) {
            logger.error("Falha ao atualizar o agendamento");
            return ResponseEntity.status(404).body("Agendamento não encontrado.");
        }

        Agendamento agendamento = agendamentoExistente.get();

        boolean houveMudanca = false;
        StringBuilder descricaoMudancas = new StringBuilder();

        if (!agendamento.getData().equals(agendamentoRequest.getData())) {
            descricaoMudancas.append("Horário atualizado! ");
            houveMudanca = true;
        }

        if (!agendamento.getFkServico().getId().equals(agendamentoRequest.getFkServico())) {
            descricaoMudancas.append("Serviço atualizado! ");
            houveMudanca = true;
        }

        if (!houveMudanca) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Nenhuma alteração detectada.");
        }


        Usuario usuario = usuarioRepository.findById(agendamentoRequest.getFkUsuario())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        agendamento.setData(agendamentoRequest.getData());
        agendamento.setFkUsuario(usuario);
        agendamento.setFkServico(servicoRepository.findById(agendamentoRequest.getFkServico())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço não encontrado")));

        Agendamento agendamentoAtualizado = repository.save(agendamento);


        HistoricoAgendamento historico = new HistoricoAgendamento();
        historico.setData(agendamentoAtualizado.getData());
        historico.setStatusAnterior("Agendado");
        historico.setStatusAtual(descricaoMudancas.toString());
        historico.setNomeUsuario(agendamentoAtualizado.getFkUsuario() != null ? agendamentoAtualizado.getFkUsuario().getNome() : "Usuário desconhecido");
        historico.setNomeServico(agendamentoAtualizado.getFkServico() != null ? agendamentoAtualizado.getFkServico().getNome() : "Serviço não informado");

        historicoRepository.save(historico);

        AgendamentoResponseDTO responseDTO = mapper.toAgendamentoResponseDto(agendamentoAtualizado);

        if (responseDTO == null) {
            logger.error("Falha ao atualizar o Agendamento");
            return ResponseEntity.status(400).build();
        }

        logger.info("Agendamento atualizado com sucesso: " + responseDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDTO);
    }


    @Transactional
    public ResponseEntity<?> removerAgendamento(Integer id) {
        Optional<Agendamento> agendamentoExistente = repository.findById(id);

        if (agendamentoExistente.isEmpty()) {
            logger.error("Falha ao deletar o agendamento");
            return ResponseEntity.status(404).body("Agendamento não encontrado.");
        }

        Agendamento agendamento = agendamentoExistente.get();

        if (agendamento.getId() == null) {
            throw new IllegalArgumentException("O ID do agendamento é nulo.");
        }

        HistoricoAgendamento historico = new HistoricoAgendamento();
        historico.setData(agendamento.getData());
        historico.setStatusAnterior("Ativo");
        historico.setStatusAtual("Cancelado");
        historico.setNomeUsuario(
                agendamento.getFkUsuario() != null ? agendamento.getFkUsuario().getNome() : "Usuário desconhecido"
        );
        historico.setNomeServico(
                agendamento.getFkServico() != null ? agendamento.getFkServico().getNome() : "Serviço não informado"
        );

        historicoRepository.save(historico);

        repository.delete(agendamento);

        logger.info("Agendamento deletado com sucesso: " + agendamento.getData());
        return ResponseEntity.status(200).body("Agendamento deletado e armazenado no histórico.");
    }




}
