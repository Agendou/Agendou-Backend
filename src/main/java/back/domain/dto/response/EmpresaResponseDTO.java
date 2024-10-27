package back.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmpresaResponseDTO {

    private Integer id;
    private String nomeEmpresa;
    private String representanteLegal;
    private String cnpj;
    private String telefone;
    private String email;
    private String senha;
}
