package back.domain.mapper;

import back.domain.dto.request.UsuarioRequestDTO;
import back.domain.dto.response.UsuarioResponseDTO;
import back.domain.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toEntity(UsuarioRequestDTO usuarioRequestDTO) {
        Usuario result = null;
        if (usuarioRequestDTO != null) {
            Usuario usuario = new Usuario();
            usuario.setNome(usuarioRequestDTO.getNome());
            usuario.setEmail(usuarioRequestDTO.getEmail());
            usuario.setSenha(usuarioRequestDTO.getSenha());
            usuario.setTelefone(usuarioRequestDTO.getTelefone());
            result = usuario;
        }

        return result;
    }

    public UsuarioResponseDTO toUsuarioResponseDto(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        UsuarioResponseDTO usuarioResponseDTO = new UsuarioResponseDTO();
        usuarioResponseDTO.setId(usuario.getId());
        usuarioResponseDTO.setNome(usuario.getNome());
        usuarioResponseDTO.setEmail(usuario.getEmail());
        usuarioResponseDTO.setSenha(usuario.getSenha());
        usuarioResponseDTO.setTelefone(usuario.getTelefone());
        usuarioResponseDTO.setRole(usuario.getRole());

        return usuarioResponseDTO;
    }
}