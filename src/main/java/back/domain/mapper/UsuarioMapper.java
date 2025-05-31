package back.domain.mapper;

import back.domain.dto.request.UsuarioRequestDTO;
import back.domain.dto.response.UsuarioResponseDTO;
import back.domain.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toEntity(UsuarioRequestDTO usuarioRequestDTO) {
        if(usuarioRequestDTO == null) return null;

        Usuario usuario = new Usuario();
        usuario.setId(usuarioRequestDTO.getId());
        usuario.setNome(usuarioRequestDTO.getNome());
        usuario.setEmail(usuarioRequestDTO.getEmail());
        usuario.setSenha(usuarioRequestDTO.getSenha());
        usuario.setTelefone(usuarioRequestDTO.getTelefone());
        usuario.setRole(usuarioRequestDTO.getRole());

        return usuario;
    }

    public UsuarioResponseDTO toUsuarioResponseDto(Usuario usuario) {
        if (usuario == null) return null;

        UsuarioResponseDTO usuarioResponseDTO = new UsuarioResponseDTO();
        usuarioResponseDTO.setId(usuario.getId());
        usuarioResponseDTO.setNome(usuario.getNome());
        usuarioResponseDTO.setEmail(usuario.getEmail());
        usuarioResponseDTO.setSenha(usuario.getSenha());
        usuarioResponseDTO.setTelefone(usuario.getTelefone());
        usuarioResponseDTO.setRole(usuario.getRole());

        usuarioResponseDTO.setToken(null);

        return usuarioResponseDTO;
    }
}