package back.domain.repository;


import back.domain.enums.StatusAgendamento;
import back.domain.model.Agendamento;
import back.domain.model.HistoricoAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Integer> {

    Optional<Agendamento> findByDataAndStatusNot(LocalDateTime data, StatusAgendamento status);

    List<Agendamento> findByFkEmpresaIdAndDataBetweenAndStatusNot(Integer empresaId, LocalDateTime dataInicio, LocalDateTime dataFim, StatusAgendamento status);

    Optional<Agendamento> findByIdAndStatusNot(Integer id, StatusAgendamento status);

    List<Agendamento> findByFkUsuarioIdAndStatusNot(Integer idUsuario, StatusAgendamento status);

    List<Agendamento> findByStatusNot(StatusAgendamento status);

    List<Agendamento> findByFkEmpresaIdAndStatusNot(Integer empresaId, StatusAgendamento status);

    Optional<Agendamento> findByIdAndStatus(Integer id, StatusAgendamento status);

    @Query("SELECT FUNCTION('MONTH', a.data) AS mes, COUNT(a.id) AS total " +
            "FROM Agendamento a " +
            "WHERE a.fkEmpresa.id = :empresaId " +
            "GROUP BY FUNCTION('MONTH', a.data) " +
            "ORDER BY FUNCTION('MONTH', a.data)")
    List<Object[]> findAgendamentosPorMesPorEmpresa(@Param("empresaId") Integer empresaId);

    @Query("SELECT s.nome, s.preco, COUNT(a) AS totalAgendamentos " +
            "FROM Agendamento a " +
            "JOIN a.fkServico s " +
            "WHERE a.fkEmpresa.id = :empresaId " +
            "GROUP BY s.id, s.nome, s.preco " +
            "ORDER BY totalAgendamentos DESC")
    List<Object[]> findServicosMaisRequisitadosPorEmpresa(@Param("empresaId") Integer empresaId);

    @Query("SELECT EXTRACT(HOUR FROM a.data) AS hora, COUNT(a) " +
            "FROM Agendamento a " +
            "WHERE a.fkEmpresa.id = :empresaId " +
            "GROUP BY EXTRACT(HOUR FROM a.data) " +
            "ORDER BY EXTRACT(HOUR FROM a.data)")
    List<Object[]> findHorariosPicoPorEmpresa(@Param("empresaId") Integer empresaId);

    @Query("SELECT COALESCE(SUM(s.preco), 0) " +
            "FROM Agendamento a " +
            "JOIN a.fkServico s " +
            "WHERE a.status IN (:status1, :status2) AND a.fkEmpresa.id = :empresaId")
    BigDecimal calcularGanhoTotalPorEmpresa(@Param("status1") StatusAgendamento status1,
                                            @Param("status2") StatusAgendamento status2,
                                            @Param("empresaId") Integer empresaId);

    @Query("SELECT COUNT(DISTINCT a.fkUsuario.id) " +
            "FROM Agendamento a " +
            "WHERE a.fkEmpresa.id = :empresaId " +
            "AND a.data BETWEEN :inicioDoMes AND :fimDoMes " +
            "AND a.data = (" +
            "   SELECT MIN(a2.data) FROM Agendamento a2 " +
            "   WHERE a2.fkUsuario.id = a.fkUsuario.id AND a2.fkEmpresa.id = :empresaId" +
            ")")
    Long countNovosClientesPorEmpresa(@Param("empresaId") Integer empresaId,
                                      @Param("inicioDoMes") LocalDateTime inicioDoMes,
                                      @Param("fimDoMes") LocalDateTime fimDoMes);
}
