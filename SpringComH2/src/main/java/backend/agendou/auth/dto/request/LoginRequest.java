package backend.agendou.auth.dto.request;

import lombok.Getter;

@Getter
public class LoginRequest {
    private String email;
    private String senha;
}
