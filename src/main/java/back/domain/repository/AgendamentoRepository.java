package back.domain.repository;


import back.domain.dto.response.AgendamentoPorMesDTO;
import back.domain.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Integer> {

    Optional<Agendamento> findByData(LocalDateTime data);
    List<Agendamento> findAll();
    Optional<Agendamento> findById(Integer id);

    @Query("SELECT COUNT(a) FROM Agendamento a WHERE a.fkUsuario.id = :fkUsuario AND a.data BETWEEN :dataInicio AND :dataFim")
    Long countAgendamentosPorUsuarioNoPeriodo(@Param("fkUsuario") Integer fkUsuario,
                                              @Param("dataInicio") LocalDateTime dataInicio,
                                              @Param("dataFim") LocalDateTime dataFim);


    List<Agendamento> findByDataBetween(LocalDateTime datInicio, LocalDateTime dataFim);

    @Query("SELECT FUNCTION('MONTH', a.data) as mes, COUNT(a.id) as total " +
            "FROM Agendamento a " +
            "GROUP BY FUNCTION('MONTH', a.data) " +
            "ORDER BY FUNCTION('MONTH', a.data)")
    List<Object[]> findAgendamentosPorMes();

    @Query("SELECT s.nome, COUNT(a) AS totalAgendamentos " +
            "FROM Agendamento a " +
            "JOIN a.fkServico s " +
            "GROUP BY s.id, s.nome " +
            "ORDER BY totalAgendamentos DESC")
    List<Object[]> findServicosMaisRequisitados();

    @Query("SELECT EXTRACT(HOUR FROM a.data) AS hora, COUNT(a) FROM Agendamento a GROUP BY EXTRACT(HOUR FROM a.data) ORDER BY EXTRACT(HOUR FROM a.data)")
    List<Object[]> findHorariosPico();

    List<Agendamento> findAllByFkUsuarioId(Integer idUsuario);


}
