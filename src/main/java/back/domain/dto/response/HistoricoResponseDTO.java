package back.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HistoricoResponseDTO {

    private Integer id;
    private LocalDateTime data;
    private String statusAnterior;
    private String statusAtual;
    private AgendamentoSimplificadoResponseDTO agendamento;
}