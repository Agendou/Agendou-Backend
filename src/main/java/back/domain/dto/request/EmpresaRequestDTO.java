package back.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmpresaRequestDTO {

    private String nomeEmpresa;
    private String representante;
    private String email;
    private String senha;
    private String telefone;
    private String cnpj;
}
