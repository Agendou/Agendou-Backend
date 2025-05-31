package back.domain.dto.request;

import back.domain.enums.UsuarioRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UsuarioRequestDTO {

    private Integer id;
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private UsuarioRole role;

    public UsuarioRequestDTO(String testUser, String mail, String password, String number, UsuarioRole usuarioRole) {
    }
}