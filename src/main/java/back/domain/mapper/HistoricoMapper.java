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
        dto.setStatusAnterior(entity.getStatusAnterior());
        dto.setStatusAtual(entity.getStatusAtual());
        dto.setFkAgendamento(entity.getAgendamento());
        dto.setFkUsuario(entity.getUsuario());
        dto.setFkEmpresa(entity.getEmpresa());

        return dto;
    }


    public HistoricoAgendamento toEntity(HistoricoRequestDTO dto) {
        if (dto == null) return null;

        HistoricoAgendamento entity = new HistoricoAgendamento();
        entity.setData(dto.getData());
        entity.setStatusAnterior(dto.getStatusAnterior());
        entity.setStatusAtual(dto.getStatusAtual());

        if (dto.getIdAgendamento() != null) {
            entity.setAgendamento(new Agendamento(dto.getIdAgendamento()));
        }

        if (dto.getIdEmpresa() != null) {
            Empresa empresa = new Empresa();
            empresa.setId(dto.getIdEmpresa());
            entity.setEmpresa(empresa);
        }

        if (dto.getIdUsuario() != null) {
            Usuario usuario = new Usuario();
            usuario.setId(dto.getIdUsuario());
            entity.setUsuario(usuario);
        }
        return entity;
    }
}
