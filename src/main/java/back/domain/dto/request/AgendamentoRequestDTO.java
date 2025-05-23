package back.domain.dto.request;

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
    private Integer fkUsuarioId;
    private Integer fkServicoId;
    private Integer fkEmpresaId;
}
