package back.domain.dto.request;

import back.domain.model.Avaliacao;
import back.domain.model.Funcionario;
import back.domain.model.Servico;
import back.domain.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AgendamentoRequestDTO {

    private Integer id;
    private LocalDateTime data;
    private Integer fkFuncionario;
    private Integer fkUsuario;
    private List<Integer> fkServicos;
}
