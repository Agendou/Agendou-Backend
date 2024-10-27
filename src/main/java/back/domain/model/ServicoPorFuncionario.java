package back.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "servico_por_agendamento")
public class ServicoPorFuncionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servico_por_funcionario")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "fk_Servico")
    private Servico fk_servico;

    @OneToOne
    @JoinColumn(name = "fk_Funcionario")
    private Funcionario fk_funcionario;
}
