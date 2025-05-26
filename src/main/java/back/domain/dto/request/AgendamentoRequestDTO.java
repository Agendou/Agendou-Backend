package back.domain.dto.request;

import back.domain.enums.StatusAgendamento;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AgendamentoRequestDTO {

    private Integer id;
    private LocalDateTime data;
    private String descricao;
    private StatusAgendamento status;
    private Integer fkUsuario;
    private Integer fkServico;
    private Integer fkEmpresa;
}
