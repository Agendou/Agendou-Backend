package back.domain.dto.response;

import back.domain.enums.StatusAgendamento;
import back.domain.enums.UsuarioRole;
import back.domain.model.Agendamento;
import back.domain.model.Empresa;
import back.domain.model.Servico;
import back.domain.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoricoResponseDTO {

    private Integer id;
    private LocalDateTime data;
    private StatusAgendamento statusAnterior;
    private StatusAgendamento statusAtual;
    private Agendamento fkAgendamento;
    private Usuario fkUsuario;
    private Empresa fkEmpresa;

    public HistoricoResponseDTO(Integer id, LocalDateTime data, StatusAgendamento statusAnterior, StatusAgendamento statusAtual, String nome, String nome1) {
    }

    public HistoricoResponseDTO(Integer id, LocalDateTime data, StatusAgendamento statusAnterior, StatusAgendamento statusAtual, AgendamentoSimplificadoResponseDTO agendamentoSimplificado, Integer id1, String nomeEmpresa, Integer id2, String nome) {
    }
}