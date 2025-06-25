package ProjCRUDJDBC.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data // Gera getters, setters, toString, equals e hashCode
@NoArgsConstructor // Gera um construtor sem argumentos
@ToString(callSuper = true) // Inclui os campos da superclasse no toString()
@EqualsAndHashCode(callSuper = true) // Inclui os campos da superclasse no equals() e hashCode()
public class Funcionario extends Pessoa {
    private String matricula;
    private String departamento;

    // Construtor customizado para facilitar a criação
    public Funcionario(Integer id, String nome, String email, String matricula, String departamento) {
        super(id, nome, email);
        this.matricula = matricula;
        this.departamento = departamento;
    }
}
