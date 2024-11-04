package back.api.config.security;

import back.domain.model.Usuario;
import back.domain.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Collections;

@AllArgsConstructor
@Component
public class SecurityFilter extends OncePerRequestFilter {

    TokenService tokenService;
    UsuarioRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response){
        var token = this.recoverToken(request);
        var login = tokenService.validateToken(token);

        if(login != null){
            Usuario user = repository.findByEmail(login);
            var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        }
    }
}
