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

    @ManyToOne
    @JoinColumn(name = "fk_funcionario")
    @Column(name = "fk_funcionario")
    private Funcionario fk_funcionario;

    @Column(name = "fk_servico")
    private Integer fk_servico;
}
