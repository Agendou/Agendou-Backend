package back.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "servicos_vinculados")
public class ServicosVinculados {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servicos_vinculados")
    private Integer id;

    @Column(name = "fk_funcionario")
    private Integer fk_funcionario;

    @Column(name = "fk_servico")
    private Integer fk_servico;
}
