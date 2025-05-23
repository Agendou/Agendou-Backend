package back.domain.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class HistoricoAgendamentoDetalhadoResponseDTO {

    private Integer id;
    private String statusAnterior;
    private String statusAtual;

    private String nomeUsuario;
    private String nomeServico;
    private String dataFormatada;
    private String horaFormatada;


}
