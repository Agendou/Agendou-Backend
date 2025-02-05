package back.domain.dto.request;

import back.domain.dto.response.AgendamentoSimplificadoResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class HistoricoRequestDTO {

    private LocalDateTime data;
    private Integer idAgendamento;
    private String statusAnterior;
    private String statusAtual;
    private Integer fkFuncionario;
    private Integer fkEmpresa;
    private Integer fkUsuario;
    private Integer fkAgendamento;
}
