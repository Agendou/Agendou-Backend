package back.domain.dto.response;

import back.domain.model.Empresa;
import back.domain.model.Servico;
import back.domain.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AgendamentoResponseDTO {

        private Integer id;
        private LocalDateTime data;
        private String descricao;
        private Usuario fkUsuario;
        private Servico fkServico;
        private Empresa fkEmpresa;
}
