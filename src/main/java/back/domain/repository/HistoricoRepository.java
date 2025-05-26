package back.domain.repository;

import back.domain.model.HistoricoAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface HistoricoRepository extends JpaRepository<HistoricoAgendamento, Integer> {

    List<HistoricoAgendamento> findByDataBetween(LocalDateTime dataInicio, LocalDateTime dataFim);

    List<HistoricoAgendamento> findByStatusAtual(String status);

    List<HistoricoAgendamento> findByAgendamentoId(Integer idAgendamento);

    List<HistoricoAgendamento> findByDataAfter(LocalDateTime data);

    List<HistoricoAgendamento> findAll();

    Optional<HistoricoAgendamento> findById(Integer id);

    @Query("SELECT h.usuario.id " +
            "FROM HistoricoAgendamento h " +
            "WHERE h.data BETWEEN :startDate AND :endDate " +
            "GROUP BY h.usuario.id " +
            "HAVING COUNT(h) >= 4")
    List<String> findActiveUsers(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(h) FROM HistoricoAgendamento h WHERE h.statusAtual = 'Cancelado'")
    Long countCancelados();

    @Query("SELECT FUNCTION('TO_CHAR', h.data, 'Month') AS mes, COUNT(h.id) AS total " +
            "FROM HistoricoAgendamento h " +
            "GROUP BY FUNCTION('TO_CHAR', h.data, 'Month'), FUNCTION('TO_CHAR', h.data, 'YYYY') " +
            "ORDER BY FUNCTION('TO_CHAR', h.data, 'YYYY'), FUNCTION('TO_CHAR', h.data, 'MM')")
    List<Object[]> totalAgendamentosPorMes();


}
