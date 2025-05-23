package back.domain.mapper;

import back.domain.dto.request.HistoricoRequestDTO;
import back.domain.dto.response.AgendamentoSimplificadoResponseDTO;
import back.domain.dto.response.HistoricoResponseDTO;
import back.domain.model.Agendamento;
import back.domain.model.Empresa;
import back.domain.model.HistoricoAgendamento;
import back.domain.model.Usuario;
import org.springframework.stereotype.Component;

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
}
