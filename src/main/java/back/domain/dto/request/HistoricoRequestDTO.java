package back.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoricoRequestDTO {
    private LocalDateTime data;
    private Integer idAgendamento;
    private String statusAnterior;
    private String statusAtual;
}
