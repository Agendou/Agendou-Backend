package back.domain.dto.response;

import back.domain.model.*;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AgendamentoResponseDTO {

    private Integer id;
    private String descricao;
    private LocalDateTime data;
    private EmpresaResponseDTO fk_empresa;
    private UsuarioResponseDTO fk_usuario;
    private ServicoResponseDTO fk_servico;
//    private AvaliacaoResponseDTO fk_avaliacao;
//    private FuncionarioResponseDTO fk_funcionario;
}
