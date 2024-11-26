package back.domain.dto.request;

import back.domain.model.Funcionario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ServicosVinculadosRequestDTO {
    private Integer id;
    private Funcionario fk_funcionario;
    private Integer fk_servico;
}
