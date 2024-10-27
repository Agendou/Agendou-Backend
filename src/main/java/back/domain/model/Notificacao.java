package back.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "notificacao")
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacao")
    private Integer id;

    @Column(name = "data")
    private LocalDateTime data;

    @Column(name = "tipo_notificacao")
    private String tipoNotificacao;

    @ManyToOne
    @JoinColumn(name = "fk_Funcionario")
    private Funcionario fk_funcionario;

    @ManyToOne
    @JoinColumn(name = "fk_Empresa")
    private Empresa fk_empresa;

    @ManyToOne
    @JoinColumn(name = "fk_Usuario")
    private Usuario fk_usuario;

    @ManyToOne
    @JoinColumn(name = "fk_Agendamento")
    private Agendamento fk_agendamento;

    @ManyToOne
    @JoinColumn(name = "fk_Servico")
    private Servico fk_servico;
}
