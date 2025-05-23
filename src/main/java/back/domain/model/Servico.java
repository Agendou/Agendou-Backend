package back.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "servico")
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servico")
    private Integer id;

    @Column(name = "nome", nullable = false, length = 60)
    private String nome;

    @Column(name = "descricao", length = 100)
    private String descricao;

    @Column(name = "preco", precision = 10, scale = 2)
    private Double preco;
}
