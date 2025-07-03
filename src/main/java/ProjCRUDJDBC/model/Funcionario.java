package ProjCRUDJDBC.model;

import lombok.*;

@Getter // Gera todos os métodos getter para os campos desta classe (getMatricula, getDepartamento).
@Setter // Gera todos os métodos setter para os campos desta classe (setMatricula, setDepartamento).
@NoArgsConstructor // Gera um construtor público sem argumentos: new Funcionario()
@ToString(callSuper = true) // Modifica o método toString() para incluir também os campos da superclasse (Pessoa).
@EqualsAndHashCode(callSuper = true) // Modifica os métodos equals() e hashCode() para considerar também os campos da superclasse.
public class Funcionario extends Pessoa {

    private String matricula;

    private String departamento;

    public Funcionario(Integer id, String nome, String email, String matricula, String departamento) {
        super(id, nome, email);
        this.matricula = matricula;
        this.departamento = departamento;
    }
}