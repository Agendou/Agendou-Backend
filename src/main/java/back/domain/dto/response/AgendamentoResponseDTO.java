package back.domain.dto.response;

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
public class AgendamentoResponseDTO {

        private Integer id;
        private LocalDateTime data;
        private Funcionario fkFuncionario;
        private Usuario fkUsuario;
        private List<Servico> fkServicos;
        private Avaliacao fkAvaliacao;
}
