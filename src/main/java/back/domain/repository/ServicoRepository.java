package back.domain.repository;

import back.domain.model.Servico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServicoRepository extends JpaRepository<Servico, Integer> {
    List<Servico> findAllByIdIn(List<Integer> ids);
    Optional<Servico> findById(Integer id);
}
