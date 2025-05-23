package back.domain.model;

import back.domain.enums.StatusAgendamento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    @Column(name = "descricao")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusAgendamento status;

    @ManyToOne
    @JoinColumn(name = "fk_usuario", referencedColumnName = "id_usuario")
    private Usuario fkUsuario;

    @ManyToOne
    @JoinColumn(name = "fk_servico", referencedColumnName = "id_servico")
    private Servico fkServico;

    @ManyToOne
    @JoinColumn(name = "fk_empresa", referencedColumnName = "id_empresa")
    private Empresa fkEmpresa;

    public Agendamento(Integer id) {
        this.id = id;
    }
}
