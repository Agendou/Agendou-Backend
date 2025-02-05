package back.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class FuncionarioRequestDTO {

    private String nome;
    private String email;
    private String senha;
    private String telefone;
}
