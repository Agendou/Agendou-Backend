package back.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotificacaoResponseDTO {

    private Integer id;
    private LocalDateTime data;
    private String tipoNotificacao;
    private Integer agendamentoId;
}
