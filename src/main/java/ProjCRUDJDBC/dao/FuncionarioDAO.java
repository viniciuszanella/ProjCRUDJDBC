package ProjCRUDJDBC.dao;

import ProjCRUDJDBC.db.Conexao;
import ProjCRUDJDBC.model.Funcionario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) para a entidade Funcionario.
 * Gerencia as operações de CRUD para funcionários e implementa as regras de negócio relacionadas.
 */
public class FuncionarioDAO {

    /**
     * Cria um novo registro de funcionário no banco.
     * REGRA DE NEGÓCIO 1: Verifica se a pessoa correspondente ao ID já existe antes de criar.
     * @param funcionario O objeto Funcionario a ser salvo.
     */
    public void create(Funcionario funcionario) {
        // Validação da regra de negócio: a pessoa base deve existir.
        PessoaDAO pessoaDAO = new PessoaDAO();
        if (pessoaDAO.readById(funcionario.getId()) == null) {
            throw new RuntimeException("Falha ao cadastrar funcionário: Pessoa com ID " + funcionario.getId() + " não existe.");
        }

        String sql = "INSERT INTO funcionario (id_pessoa, matricula, departamento) VALUES (?, ?, ?)";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, funcionario.getId());
            pstmt.setString(2, funcionario.getMatricula());
            pstmt.setString(3, funcionario.getDepartamento());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir funcionário no banco de dados: " + e.getMessage(), e);
        }
    }

    /**
     * Deleta um funcionário do banco de dados.
     * REGRA DE NEGÓCIO 3: Proíbe a exclusão se o funcionário estiver vinculado a algum projeto.
     * @param id_pessoa O ID do funcionário a ser deletado.
     */
    public void delete(int id_pessoa) {
        // Validação da regra de negócio: o funcionário não pode ter projetos.
        ProjetoDAO projetoDAO = new ProjetoDAO();
        if (projetoDAO.countProjetosByFuncionario(id_pessoa) > 0) {
            throw new RuntimeException("Falha ao excluir: Funcionário está vinculado a um ou mais projetos.");
        }

        String sql = "DELETE FROM funcionario WHERE id_pessoa = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id_pessoa);
            int affectedRows = pstmt.executeUpdate();

            // Lança uma exceção se nenhum funcionário com o ID fornecido for encontrado.
            if(affectedRows == 0){
                throw new RuntimeException("Falha ao excluir: Funcionário com ID " + id_pessoa + " não encontrado.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar funcionário: " + e.getMessage(), e);
        }
    }

    /**
     * Busca um funcionário pelo seu ID (que é o mesmo ID da pessoa).
     * @param id_pessoa O ID do funcionário a ser buscado.
     * @return Um objeto Funcionario completo (com dados de Pessoa) se encontrado, senão null.
     */
    public Funcionario readById(int id_pessoa) {
        // A consulta SQL une as tabelas 'pessoa' e 'funcionario' para obter todos os dados.
        String sql = "SELECT p.id, p.nome, p.email, f.matricula, f.departamento " +
                "FROM pessoa p JOIN funcionario f ON p.id = f.id_pessoa WHERE f.id_pessoa = ?";
        Funcionario funcionario = null;
        try (Connection conn = Conexao.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id_pessoa);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                // Popula o objeto Funcionario com os dados do ResultSet.
                funcionario = new Funcionario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("matricula"),
                        rs.getString("departamento")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar funcionário por ID: " + e.getMessage(), e);
        }
        return funcionario;
    }

    /**
     * Retorna uma lista com todos os funcionários cadastrados.
     * @return Uma lista de objetos Funcionario.
     */
    public List<Funcionario> findAll() {
        List<Funcionario> funcionarios = new ArrayList<>();
        String sql = "SELECT p.id, p.nome, p.email, f.matricula, f.departamento " +
                "FROM funcionario f JOIN pessoa p ON f.id_pessoa = p.id ORDER BY p.id";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Funcionario funcionario = new Funcionario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("matricula"),
                        rs.getString("departamento")
                );
                funcionarios.add(funcionario);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Falha ao buscar funcionários no banco de dados.", e);
        }
        return funcionarios;
    }
}