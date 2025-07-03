package ProjCRUDJDBC;

import ProjCRUDJDBC.dao.FuncionarioDAO;
import ProjCRUDJDBC.dao.PessoaDAO;
import ProjCRUDJDBC.dao.ProjetoDAO;
import ProjCRUDJDBC.model.Funcionario;
import ProjCRUDJDBC.model.Pessoa;
import ProjCRUDJDBC.model.Projeto;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final PessoaDAO pessoaDAO = new PessoaDAO();
    private static final FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
    private static final ProjetoDAO projetoDAO = new ProjetoDAO();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int opcao;
        // Loop principal do menu. Continua executando até que a opção 0 (Sair) seja escolhida.
        do {
            exibirMenu();
            // Validação para garantir que a entrada do usuário seja um número inteiro.
            while (!scanner.hasNextInt()) {
                System.out.println("Opção inválida. Por favor, digite um número.");
                scanner.next();
                System.out.print("Escolha uma opção: ");
            }
            opcao = scanner.nextInt();
            scanner.nextLine();

            try {
                // Direciona a execução para o método correspondente à opção escolhida.
                roteador(opcao);
            } catch (RuntimeException e) {
                // Captura exceções inesperadas para evitar que a aplicação pare.
                System.err.println("\nERRO INESPERADO: " + e.getMessage());
                e.printStackTrace(); // Imprime o stack trace para ajudar na depuração.
            }

        } while (opcao != 0);

        System.out.println("--- Fim da Aplicação ---");
        scanner.close();
    }

    /**
     * Imprime as opções do menu principal no console.
     */
    private static void exibirMenu() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1. Cadastrar Pessoa");
        System.out.println("2. Listar Pessoas");
        System.out.println("3. Cadastrar Funcionário");
        System.out.println("4. Listar Funcionários");
        System.out.println("5. Excluir Funcionário");
        System.out.println("6. Cadastrar Projeto");
        System.out.println("7. Listar Projetos");
        System.out.println("8. Excluir Projeto");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static void roteador(int opcao) {
        switch (opcao) {
            case 1:
                cadastrarPessoa();
                break;
            case 2:
                listarPessoas();
                break;
            case 3:
                cadastrarFuncionario();
                break;
            case 4:
                listarFuncionarios();
                break;
            case 5:
                excluirFuncionario();
                break;
            case 6:
                cadastrarProjeto();
                break;
            case 7:
                listarProjetos();
                break;
            case 8:
                excluirProjeto();
                break;
            case 0:
                // Nenhuma ação necessária, o loop principal cuidará do encerramento.
                break;
            default:
                System.out.println("Opção inválida. Tente novamente.");
        }
        // Pausa a execução para que o usuário possa ler a saída antes de o menu ser exibido novamente.
        if (opcao != 0) {
            System.out.println("\nPressione Enter para continuar...");
            scanner.nextLine();
        }
    }

    /**
     * Coleta os dados do usuário e chama o DAO para cadastrar uma nova pessoa.
     */
    private static void cadastrarPessoa() {
        System.out.println("\n--- Cadastro de Nova Pessoa ---");
        System.out.print("Digite o nome: ");
        String nome = scanner.nextLine();
        System.out.print("Digite o email: ");
        String email = scanner.nextLine();

        // Cria um objeto Pessoa (com ID nulo, pois será gerado pelo banco) e o envia para o DAO.
        Pessoa novaPessoa = new Pessoa(null, nome, email);
        pessoaDAO.create(novaPessoa);
        System.out.println("SUCESSO: Pessoa cadastrada com sucesso!");
    }

    /**
     * Busca e exibe todas as pessoas cadastradas no banco de dados.
     */
    private static void listarPessoas() {
        System.out.println("\n--- Lista de Pessoas Cadastradas ---");
        List<Pessoa> pessoas = pessoaDAO.findAll();
        if (pessoas.isEmpty()) {
            System.out.println("Nenhuma pessoa encontrada.");
        } else {
            // Itera sobre a lista de pessoas e imprime cada uma usando o método toString() do modelo.
            pessoas.forEach(System.out::println);
        }
    }

    /**
     * Coleta os dados e chama o DAO para cadastrar um novo funcionário,
     * vinculando-o a uma pessoa existente.
     */
    private static void cadastrarFuncionario() {
        System.out.println("\n--- Cadastro de Novo Funcionário ---");
        System.out.println("Pessoas disponíveis para se tornarem funcionários:");
        listarPessoas(); // Mostra as pessoas para facilitar a escolha do ID.

        System.out.print("\nDigite o ID da Pessoa para vincular como funcionário: ");
        int pessoaId = scanner.nextInt();
        scanner.nextLine(); // Consome a quebra de linha.

        System.out.print("Digite a matrícula: ");
        String matricula = scanner.nextLine();
        System.out.print("Digite o departamento: ");
        String departamento = scanner.nextLine();

        // Cria o objeto Funcionario e define seus atributos.
        Funcionario novoFuncionario = new Funcionario();
        novoFuncionario.setId(pessoaId); // O ID é herdado de Pessoa.
        novoFuncionario.setMatricula(matricula);
        novoFuncionario.setDepartamento(departamento);

        try {
            // Tenta criar o funcionário. O DAO cuidará da validação (Regra de Negócio 1).
            funcionarioDAO.create(novoFuncionario);
            System.out.println("SUCESSO: Funcionário cadastrado com sucesso!");
        } catch (RuntimeException e) {
            // Captura e exibe a mensagem de erro vinda do DAO.
            System.err.println("ERRO AO CADASTRAR: " + e.getMessage());
        }
    }

    /**
     * Busca e exibe todos os funcionários cadastrados.
     */
    private static void listarFuncionarios() {
        System.out.println("\n--- Lista de Funcionários Cadastrados ---");
        List<Funcionario> funcionarios = funcionarioDAO.findAll();
        if (funcionarios.isEmpty()) {
            System.out.println("Nenhum funcionário encontrado.");
        } else {
            funcionarios.forEach(System.out::println);
        }
    }

    /**
     * Solicita o ID de um funcionário e chama o DAO para excluí-lo.
     */
    private static void excluirFuncionario() {
        System.out.println("\n--- Exclusão de Funcionário ---");
        listarFuncionarios(); // Mostra os funcionários para facilitar a escolha.

        System.out.print("\nDigite o ID do funcionário que deseja excluir: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consome a quebra de linha.

        try {
            // Tenta excluir o funcionário. O DAO cuidará da validação (Regra de Negócio 3).
            funcionarioDAO.delete(id);
            System.out.println("SUCESSO: Funcionário com ID " + id + " excluído!");
        } catch (RuntimeException e) {
            System.err.println("ERRO AO EXCLUIR: " + e.getMessage());
        }
    }

    /**
     * Coleta os dados de um novo projeto e chama o DAO para cadastrá-lo.
     */
    private static void cadastrarProjeto() {
        System.out.println("\n--- Cadastro de Novo Projeto ---");
        listarFuncionarios(); // Mostra os funcionários para facilitar a atribuição de um responsável.

        System.out.print("\nDigite o nome do projeto: ");
        String nome = scanner.nextLine();
        System.out.print("Digite a descrição do projeto: ");
        String descricao = scanner.nextLine();
        System.out.print("Digite o ID do funcionário responsável: ");
        int funcId = scanner.nextInt();
        scanner.nextLine(); // Consome a quebra de linha.

        // Cria o objeto Projeto.
        Projeto novoProjeto = new Projeto(null, nome, descricao, funcId);

        try {
            // Tenta criar o projeto. O DAO cuidará da validação (Regra de Negócio 2).
            projetoDAO.create(novoProjeto);
            System.out.println("SUCESSO: Projeto cadastrado com sucesso!");
        } catch (RuntimeException e) {
            System.err.println("ERRO AO CADASTRAR: " + e.getMessage());
        }
    }

    /**
     * Busca e exibe todos os projetos cadastrados.
     */
    private static void listarProjetos() {
        System.out.println("\n--- Lista de Projetos Cadastrados ---");
        List<Projeto> projetos = projetoDAO.findAll();
        if (projetos.isEmpty()) {
            System.out.println("Nenhum projeto encontrado.");
        } else {
            projetos.forEach(System.out::println);
        }
    }

    /**
     * Solicita o ID de um projeto e chama o DAO para excluí-lo.
     */
    private static void excluirProjeto() {
        System.out.println("\n--- Exclusão de Projeto ---");
        listarProjetos(); // Mostra os projetos para facilitar a escolha.

        System.out.print("\nDigite o ID do projeto que deseja excluir: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consome a quebra de linha.

        try {
            // Tenta excluir o projeto.
            projetoDAO.delete(id);
            System.out.println("SUCESSO: Projeto com ID " + id + " excluído!");
        } catch (RuntimeException e) {
            System.err.println("ERRO AO EXCLUIR: " + e.getMessage());
        }
    }
}