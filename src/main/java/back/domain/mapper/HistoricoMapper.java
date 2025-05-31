package back.domain.mapper;

import back.domain.dto.request.HistoricoRequestDTO;
import back.domain.dto.response.AgendamentoSimplificadoResponseDTO;
import back.domain.dto.response.HistoricoAgendamentoDetalhadoResponseDTO;
import back.domain.dto.response.HistoricoResponseDTO;
import back.domain.model.Agendamento;
import back.domain.model.Empresa;
import back.domain.model.HistoricoAgendamento;
import back.domain.model.Usuario;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class HistoricoMapper {

    private final AgendamentoMapper agendamentoMapper;

    public HistoricoMapper(AgendamentoMapper agendamentoMapper) {
        this.agendamentoMapper = agendamentoMapper;
    }

    public HistoricoResponseDTO toHistoricoResponseDto(HistoricoAgendamento entity) {
        if (entity == null) return null;

        HistoricoResponseDTO dto = new HistoricoResponseDTO();
        dto.setId(entity.getId());
        dto.setData(entity.getData());
        dto.setStatusAnterior(entity.getStatusAnterior().name());
        dto.setStatusAtual(entity.getStatusAtual().name());

        if(entity.getAgendamento() != null && entity.getAgendamento().getFkUsuario() != null) {
            AgendamentoSimplificadoResponseDTO agendamentoSimplificado =
                    new AgendamentoSimplificadoResponseDTO(
                            entity.getAgendamento().getFkUsuario().getNome(),
                            entity.getAgendamento().getData()
                    );
            dto.setAgendamento(agendamentoSimplificado);
        }
        return dto;
    }

    public HistoricoAgendamento toEntity(HistoricoRequestDTO dto) {
        if (dto == null) return null;

        HistoricoAgendamento entity = new HistoricoAgendamento();
        entity.setData(dto.getData());
        entity.setStatusAnterior(dto.getStatusAnterior());
        entity.setStatusAtual(dto.getStatusAtual());

        if (dto.getAgendamentoId() != null) {
            entity.setAgendamento(new Agendamento(dto.getAgendamentoId()));
        }

        if (dto.getEmpresaId() != null) {
            Empresa empresa = new Empresa();
            empresa.setId(dto.getEmpresaId());
            entity.setEmpresa(empresa);
        }

        if (dto.getUsuarioId() != null) {
            Usuario usuario = new Usuario();
            usuario.setId(dto.getUsuarioId());
            entity.setUsuario(usuario);
        }
        return entity;
    }

    public HistoricoAgendamentoDetalhadoResponseDTO toHistoricoAgendamentoDetalhadoResponseDTO(HistoricoAgendamento entity) {
        if (entity == null || entity.getAgendamento() == null) return null;

        Agendamento agendamento = entity.getAgendamento();

        String nomeUsuario = agendamento.getFkUsuario() != null ? agendamento.getFkUsuario().getNome() : "Desconhecido";
        String nomeServico = agendamento.getFkServico() != null ? agendamento.getFkServico().getNome() : "Serviço não informado";

        DateTimeFormatter dataFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm");

        String dataFormatada = agendamento.getData().format(dataFormatter);
        String horaFormatada = agendamento.getData().format(horaFormatter);

        return new HistoricoAgendamentoDetalhadoResponseDTO(
                entity.getId(),
                entity.getStatusAnterior().name(),
                entity.getStatusAtual().name(),
                nomeUsuario,
                nomeServico,
                dataFormatada,
                horaFormatada
        );
    }
}
