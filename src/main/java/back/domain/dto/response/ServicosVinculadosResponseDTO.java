package back.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ServicosVinculadosResponseDTO {
    private Integer id;
    private Integer fk_funcionario;
    private Integer fk_servico;
}
