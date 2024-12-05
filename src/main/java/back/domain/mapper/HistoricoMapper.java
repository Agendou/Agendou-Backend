package back.domain.mapper;

import back.domain.dto.response.HistoricoResponseDTO;
import back.domain.model.HistoricoAgendamento;
import org.springframework.stereotype.Component;

@Component
public class HistoricoMapper {

    private final AgendamentoMapper mapper;

    public HistoricoMapper(AgendamentoMapper agendamentoMapper) {
        this.mapper = agendamentoMapper;
    }

    public HistoricoResponseDTO toHistoricoResponseDto(HistoricoAgendamento entity) {
        if (entity == null) {
            return null;
        }

        HistoricoResponseDTO dto = new HistoricoResponseDTO();
        dto.setId(entity.getId());
        dto.setData(entity.getData());
        dto.setStatusAnterior(entity.getStatusAnterior());
        dto.setStatusAtual(entity.getStatusAtual());
        dto.setAgendamento(mapper.toAgendamentoResponseDto(entity.getFkAgendamento()));

        return dto;
    }
}