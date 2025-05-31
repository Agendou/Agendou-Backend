package back.domain.dto.response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AgendamentoSimplificadoResponseDTO {

    private String nomeUsuario;
    private String data;
    private String hora;

    public AgendamentoSimplificadoResponseDTO(String nomeUsuario, LocalDateTime dataHora) {
        this.nomeUsuario = nomeUsuario;

        DateTimeFormatter dataFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.data = dataHora.format(dataFormatter);

        DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm");
        this.hora = dataHora.format(horaFormatter);
    }
}
