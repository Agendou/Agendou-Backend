package back.domain.dto.response;

import back.domain.enums.UsuarioRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UsuarioResponseDTO {

    private Integer id;
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private UsuarioRole role;
    private String token;

}