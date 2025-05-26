package back.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ServicoResponseDTO {

        private Integer id;
        private String nome;
        private Double preco;
        private String descricao;
}
