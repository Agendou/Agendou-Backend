package back.domain.dto.response;

import back.domain.model.Servico;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FuncionarioResponseDTO {

    private Integer id;
    private String nome;
    private String email;
    private String senha;
    private String telefone;
//    private Integer fk_empresa;
    private List<Servico> servicos;
}
