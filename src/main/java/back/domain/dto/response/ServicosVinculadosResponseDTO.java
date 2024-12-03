package back.domain.dto.response;

import back.domain.model.Funcionario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ServicosVinculadosResponseDTO {
    private Integer id;
    private Funcionario fk_funcionario;
    private Integer fk_servico;
}
