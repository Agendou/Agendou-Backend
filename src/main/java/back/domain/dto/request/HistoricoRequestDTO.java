package back.domain.dto.request;

import back.domain.enums.StatusAgendamento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoricoRequestDTO {

    private LocalDateTime data;
    private StatusAgendamento statusAnterior;
    private StatusAgendamento statusAtual;
    private Integer agendamentoId;
    private Integer empresaId;
    private Integer usuarioId;
}
