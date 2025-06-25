package ProjCRUDJDBC;

import ProjCRUDJDBC.dao.FuncionarioDAO;
import ProjCRUDJDBC.dao.PessoaDAO;
import ProjCRUDJDBC.dao.ProjetoDAO;
import ProjCRUDJDBC.model.Funcionario;
import ProjCRUDJDBC.model.Pessoa;
import ProjCRUDJDBC.model.Projeto;

public class Main {
    public static void main(String[] args) {
        PessoaDAO pessoaDAO = new PessoaDAO();
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
        ProjetoDAO projetoDAO = new ProjetoDAO();

        System.out.println("--- Início dos Testes da Aplicação ---");

        // --- Teste 1: Cadastro de Pessoa e Funcionário (Sucesso) ---
        System.out.println("\n[TESTE 1] Tentando cadastrar uma nova Pessoa e um Funcionário válido...");
        try {
            Pessoa novaPessoa = new Pessoa(null, "Carlos Nobre", "carlos.nobre@email.com");
            pessoaDAO.create(novaPessoa); // O ID será gerado pelo banco
            // Para vincular, precisaríamos buscar o ID, vamos usar um ID existente (11) para simplificar.

            Funcionario novoFuncionario = new Funcionario();
            novoFuncionario.setId(11); // ID da pessoa 'Laura Santos'
            novoFuncionario.setMatricula("F011");
            novoFuncionario.setDepartamento("Inovação");

            funcionarioDAO.create(novoFuncionario);
            // REGRA DE NEGÓCIO 5: Mensagem de confirmação
            System.out.println("SUCESSO: Funcionário 'Laura Santos' cadastrado com sucesso!");
        } catch (RuntimeException e) {
            System.err.println("ERRO: " + e.getMessage());
        }

        // --- Teste 2: Tentar cadastrar Funcionário com ID de Pessoa inexistente (Falha) ---
        // REGRA DE NEGÓCIO 1
        System.out.println("\n[TESTE 2] Tentando cadastrar um Funcionário com ID de Pessoa que não existe (ID 99)...");
        try {
            Funcionario funcInvalido = new Funcionario();
            funcInvalido.setId(99); // ID inexistente
            funcInvalido.setMatricula("F099");
            funcInvalido.setDepartamento("TI");
            funcionarioDAO.create(funcInvalido);
            System.out.println("SUCESSO: Funcionário inválido cadastrado.");
        } catch (RuntimeException e) {
            // REGRA DE NEGÓCIO 4: Mensagem de erro
            System.err.println("ERRO ESPERADO: " + e.getMessage());
        }

        // --- Teste 3: Tentar criar Projeto com Funcionário inexistente (Falha) ---
        // REGRA DE NEGÓCIO 2
        System.out.println("\n[TESTE 3] Tentando criar um Projeto para um Funcionário que não existe (ID 88)...");
        try {
            Projeto projInvalido = new Projeto(null, "Projeto Fantasma", "Descrição", 88);
            projetoDAO.create(projInvalido);
            System.out.println("SUCESSO: Projeto inválido criado.");
        } catch (RuntimeException e) {
            // REGRA DE NEGÓCIO 4: Mensagem de erro
            System.err.println("ERRO ESPERADO: " + e.getMessage());
        }

        // --- Teste 4: Tentar excluir Funcionário vinculado a Projeto (Falha) ---
        // REGRA DE NEGÓCIO 3
        System.out.println("\n[TESTE 4] Tentando excluir um Funcionário que tem um Projeto (ID 1)...");
        try {
            funcionarioDAO.delete(1); // Ana Silva (ID 1) está no 'Sistema de Gestão Interna'
            System.out.println("SUCESSO: Funcionário vinculado a projeto foi excluído.");
        } catch (RuntimeException e) {
            // REGRA DE NEGÓCIO 4: Mensagem de erro
            System.err.println("ERRO ESPERADO: " + e.getMessage());
        }

        // --- Teste 5: Excluir Funcionário sem projetos (Sucesso) ---
        System.out.println("\n[TESTE 5] Tentando excluir um Funcionário sem projetos vinculados (ID 11)...");
        try {
            // O funcionário com id 11 (Laura Santos) foi criado, mas não tem projetos.
            funcionarioDAO.delete(11);
            // REGRA DE NEGÓCIO 5: Mensagem de confirmação
            System.out.println("SUCESSO: Funcionário com ID 11 foi excluído com sucesso!");
        } catch (RuntimeException e) {
            System.err.println("ERRO: " + e.getMessage());
        }


        System.out.println("\n--- Fim dos Testes da Aplicação ---");
    }
}
