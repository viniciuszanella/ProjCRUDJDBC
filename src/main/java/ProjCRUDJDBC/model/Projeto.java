package ProjCRUDJDBC.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data // Gera automaticamente: getters, setters, toString(), equals() e hashCode().
@NoArgsConstructor // Gera um construtor público sem argumentos: new Projeto()
@AllArgsConstructor // Gera um construtor público com todos os campos como argumentos.
public class Projeto {

    private Integer id;


    private String nome;


    private String descricao;


    private int id_funcionario;
}