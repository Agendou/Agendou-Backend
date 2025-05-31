package back.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AgendamentoRequestDTO {

    private Integer id;
    private LocalDateTime data;
    private String descricao;
    private Integer fkUsuarioId;
    private Integer fkServicoId;
    private Integer fkEmpresaId;
}
