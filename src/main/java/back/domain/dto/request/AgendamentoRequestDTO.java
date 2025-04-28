package back.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AgendamentoRequestDTO {

    private Integer id;
    private LocalDateTime data;
    private Integer fkUsuario;
    private Integer fkServico;
}
