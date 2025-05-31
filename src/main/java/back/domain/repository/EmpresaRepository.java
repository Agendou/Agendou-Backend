package back.domain.repository;

import back.domain.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {
    Optional<Empresa> findByEmail(String email);
    Optional<Empresa> findById(Integer id);

    boolean existsByEmail(String email);
}
