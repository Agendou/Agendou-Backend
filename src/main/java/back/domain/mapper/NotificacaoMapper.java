package back.domain.mapper;

import back.domain.dto.response.NotificacaoResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class NotificacaoMapper {

    public NotificacaoResponseDTO toNotificacaoResponseDto(Notificacao entity) {
        if (entity == null) {
            return null;
        }

        NotificacaoResponseDTO dto = new NotificacaoResponseDTO();
        dto.setId(entity.getId());
        dto.setData(entity.getData());
        dto.setTipoNotificacao(entity.getTipoNotificacao());
        dto.setAgendamentoId(entity.getFkAgendamento().getId());

        return dto;
    }
}