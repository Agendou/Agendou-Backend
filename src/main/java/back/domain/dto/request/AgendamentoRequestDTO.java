package back.domain.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AgendamentoRequestDTO {

    private Integer id;
    private LocalDateTime data;
    private String descricao;
    private Integer fkEmpresa;
    private Integer fkFuncionario;
    private Integer fkUsuario;
    private Integer fkServico;
}
