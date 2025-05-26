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
@Table(name = "historico_agendamento")
public class HistoricoAgendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historico_agendamento")
    private Integer id;

    @Column(name = "data", nullable = false)
    private LocalDateTime data;

    @Column(name = "status_anterior", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusAgendamento statusAnterior;

    @Column(name = "status_atual", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusAgendamento statusAtual;

    @ManyToOne
    @JoinColumn(name = "fk_agendamento", nullable = false)
    private Agendamento agendamento;

    @ManyToOne
    @JoinColumn(name = "fk_empresa", nullable = false)
    private Empresa empresa;

    @ManyToOne
    @JoinColumn(name = "fk_usuario", nullable = false)
    private Usuario usuario;
}
