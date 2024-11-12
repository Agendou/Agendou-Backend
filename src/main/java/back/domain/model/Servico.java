package back.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @Column(name = "nome")
    private String nome;

    @Column(name = "preco")
    private Double preco;

    @Column(name = "descricao")
    private String descricao;

//    @ManyToMany
//    @JoinTable(name = "fk_funcionario",
//    joinColumns = @JoinColumn(name = "id_servicos"),
//    inverseJoinColumns = @JoinColumn(name = "id_funcionario"))
//    private List<Funcionario> funcionarios;
}
