package back.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ServicosVinculadosRequestDTO {
    private Integer id;
    private Integer fk_funcionario;
    private Integer fk_servico;
}
