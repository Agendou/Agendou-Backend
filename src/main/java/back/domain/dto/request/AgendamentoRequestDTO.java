package back.domain.dto.request;

import back.domain.dto.response.ServicoResponseDTO;
import back.domain.dto.response.UsuarioResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AgendamentoRequestDTO {

    private Integer id;
    private String descricao;
    private LocalDateTime data;
    private UsuarioRequestDTO usuario;
    private ServicoRequestDTO servico;
}
