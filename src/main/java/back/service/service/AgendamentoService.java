

package back.service.service;

import back.api.controller.HistoricoController;
import back.domain.dto.request.AgendamentoRequestDTO;
import back.domain.dto.request.HistoricoRequestDTO;
import back.domain.dto.response.AgendamentoResponseDTO;
import back.domain.dto.response.AgendamentoSimplificadoResponseDTO;
import back.domain.mapper.AgendamentoMapper;
import back.domain.model.*;
import back.domain.repository.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository repository;
    private final AgendamentoMapper mapper;
    private final ServicoRepository servicoRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final HistoricoService historicoService;

    private static final Logger logger = LoggerFactory.getLogger(AgendamentoService.class);
    private final UsuarioRepository usuarioRepository;
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
        historico.setNomeFuncionario(
                agendamentoSalvo.getFkFuncionario() != null ? agendamentoSalvo.getFkFuncionario().getNome() : "Funcionário desconhecido"
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

    public List<AgendamentoResponseDTO> listarAgendamentosPorFuncionario(Integer funcionarioId) {
        List<Agendamento> agendamentos = repository.findAllByFkFuncionarioId(funcionarioId);

        // Convert to DTO
        return agendamentos.stream()
                .map(mapper::toAgendamentoResponseDto)
                .collect(Collectors.toList());
    }

    public List<AgendamentoResponseDTO> listarAgendamentos() {
        List<Agendamento> agendamentos = repository.findAll();
        return agendamentos.stream()
                .map(mapper::toAgendamentoResponseDto)
                .collect(Collectors.toList());
    }

    public ResponseEntity<?> buscarAgendamentoPorId(Integer id) {
        List<Agendamento> agendamentos = repository.findAll();
        Collections.sort(agendamentos, Comparator.comparing(Agendamento::getId));

        int index = Collections.binarySearch(agendamentos, new Agendamento(id), Comparator.comparing(Agendamento::getId));

        if (index < 0) {
            logger.error("Agendamento com id " + id + " não encontrado");
            return ResponseEntity.status(404).body("Agendamento não encontrado");
        }

        Agendamento agendamento = agendamentos.get(index);
        AgendamentoResponseDTO responseDTO = mapper.toAgendamentoResponseDto(agendamento);

        return ResponseEntity.status(200).body(responseDTO);
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

        if (!agendamento.getFkFuncionario().getId().equals(agendamentoRequest.getFkFuncionario())) {
            descricaoMudancas.append("Profissional atualizado! ");
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
        agendamento.setFkFuncionario(funcionarioRepository.findById(agendamentoRequest.getFkFuncionario())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionário não encontrado")));

        Agendamento agendamentoAtualizado = repository.save(agendamento);


        HistoricoAgendamento historico = new HistoricoAgendamento();
        historico.setData(agendamentoAtualizado.getData());
        historico.setStatusAnterior("Agendado");
        historico.setStatusAtual(descricaoMudancas.toString());
        historico.setNomeUsuario(agendamentoAtualizado.getFkUsuario() != null ? agendamentoAtualizado.getFkUsuario().getNome() : "Usuário desconhecido");
        historico.setNomeFuncionario(agendamentoAtualizado.getFkFuncionario() != null ? agendamentoAtualizado.getFkFuncionario().getNome() : "Funcionário desconhecido");
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
        historico.setNomeFuncionario(
                agendamento.getFkFuncionario() != null ? agendamento.getFkFuncionario().getNome() : "Funcionário desconhecido"
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
