package back.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicoRequestDTO {
    private Integer id;
    private String nome;
    private Double preco;
    private String descricao;
}
