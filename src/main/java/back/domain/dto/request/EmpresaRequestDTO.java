package back.domain.dto.request;

import back.domain.enums.EmpresaRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class EmpresaRequestDTO {

    private String nome;
    private String representante;
    private String email;
    private String senha;
    private String telefone;
    private String cnpj;
//    private EmpresaRole role; -- separar um usuário da empresa e um usuário cliente
}
