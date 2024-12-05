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
    @JoinColumn(name = "fk_agendamento")
    private Agendamento fkAgendamento;
}
