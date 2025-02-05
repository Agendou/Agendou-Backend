package back.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "agendamento")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_agendamento")
    private Integer id;

    @Column(name = "data")
    private LocalDateTime data;

    @ManyToOne
    @JoinColumn(name = "fk_funcionario")
    private Funcionario fkFuncionario;

    @ManyToOne
    @JoinColumn(name = "fk_usuario")
    private Usuario fkUsuario;

    @ManyToOne
    @JoinColumn(name = "fk_servico")
    private Servico fkServico;

    @ManyToOne
    @JoinColumn(name = "fk_empresa")
    private Empresa fkEmpresa;
}