package back.domain.dto.request;

import back.domain.enums.UsuarioRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Data
public class UsuarioRequestDTO {

    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private UsuarioRole role;
    private LocalDateTime dataCadastro;

    public UsuarioRequestDTO(String testUser, String mail, String password, String number, UsuarioRole usuarioRole) {
    }
}