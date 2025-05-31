package back.domain.utils;

import back.domain.model.Empresa;
import back.domain.model.Servico;
import back.domain.model.Usuario;
import back.domain.repository.EmpresaRepository;
import back.domain.repository.ServicoRepository;
import back.domain.repository.UsuarioRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@Getter
@Setter
public class Helper {

    private final UsuarioRepository usuarioRepository;
    private final ServicoRepository servicoRepository;
    private final EmpresaRepository empresaRepository;

    public Helper(UsuarioRepository usuarioRepository, ServicoRepository servicoRepository, EmpresaRepository empresaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.servicoRepository = servicoRepository;
        this.empresaRepository = empresaRepository;
    }

    public Usuario buscarUsuario(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    public Servico buscarServico(Integer id) {
        return servicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço não encontrado"));
    }

    public Empresa buscarEmpresa(Integer id) {
        return empresaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada"));
    }
}

