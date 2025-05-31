package back.domain.repository;

import back.domain.enums.StatusAgendamento;
import back.domain.model.HistoricoAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HistoricoRepository extends JpaRepository<HistoricoAgendamento, Integer> {

    List<HistoricoAgendamento> findByEmpresaId(Integer empresaId);

    List<HistoricoAgendamento> findByDataBetween(LocalDateTime dataInicio, LocalDateTime dataFim);

    List<HistoricoAgendamento> findByStatusAtual(StatusAgendamento status);

    @Query("SELECT h FROM HistoricoAgendamento h WHERE h.empresa.id = :empresaId AND h.data BETWEEN :dataInicio AND :dataFim")
    List<HistoricoAgendamento> findByEmpresaIdAndDataBetween(
            @Param("empresaId") Integer empresaId,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim);

    @Query("SELECT h.usuario.nome " +
            "FROM HistoricoAgendamento h " +
            "WHERE h.data BETWEEN :startDate AND :endDate " +
            "AND h.empresa.id = :empresaId " +
            "GROUP BY h.usuario.nome " +
            "HAVING COUNT(h) >= 4")
    List<String> findActiveUsers(@Param("startDate") LocalDateTime startDate,
                                 @Param("endDate") LocalDateTime endDate,
                                 @Param("empresaId") Integer empresaId);

    @Query("SELECT COUNT(h) FROM HistoricoAgendamento h WHERE h.empresa.id = :empresaId AND h.statusAtual = :statusCancelado")
    Long countCanceladosPorEmpresa(@Param("empresaId") Integer empresaId,
                                   @Param("statusCancelado") StatusAgendamento statusCancelado);

    @Query(value = "SELECT DATE_FORMAT(data, '%Y-%m') AS mes, COUNT(*) AS total " +
            "FROM historico_agendamento " +
            "WHERE fk_empresa = :empresaId " +
            "GROUP BY DATE_FORMAT(data, '%Y-%m') " +
            "ORDER BY DATE_FORMAT(data, '%Y-%m')", nativeQuery = true)
    List<Object[]> totalAgendamentosPorMes(@Param("empresaId") Integer empresaId);

    List<HistoricoAgendamento> findByAgendamentoId(Integer id);
}
