package backend.agendou.auth.dto.request;

import lombok.Getter;

@Getter
public class LoginRequestDTO {
    private String email;
    private String senha;
}
