package ProjCRUDJDBC.dao;

import ProjCRUDJDBC.db.Conexao;
import ProjCRUDJDBC.model.Funcionario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FuncionarioDAO {

    /**
     * Cria um novo funcionário, validando se a pessoa base já existe.
     * REGRA DE NEGÓCIO 1: Ao cadastrar um funcionário, verificar se o ID da pessoa existe na tabela pessoa.
     * @param funcionario O funcionário a ser criado.
     */
    public void create(Funcionario funcionario) {
        // Regra de Negócio 1: Verificar se a Pessoa existe
        PessoaDAO pessoaDAO = new PessoaDAO();
        if (pessoaDAO.readById(funcionario.getId()) == null) {
            // Regra de Negócio 4: Mensagem de erro clara
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
     * Deleta um funcionário, validando se ele não está vinculado a nenhum projeto.
     * REGRA DE NEGÓCIO 3: Proibir a exclusão de um funcionário que esteja vinculado a algum projeto.
     * @param id_pessoa O ID da pessoa/funcionário a ser deletado.
     */
    public void delete(int id_pessoa) {
        // Regra de Negócio 3: Verificar se o funcionário está vinculado a projetos
        ProjetoDAO projetoDAO = new ProjetoDAO();
        if (projetoDAO.countProjetosByFuncionario(id_pessoa) > 0) {
            // Regra de Negócio 4: Mensagem de erro clara
            throw new RuntimeException("Falha ao excluir: Funcionário está vinculado a um ou mais projetos.");
        }

        String sql = "DELETE FROM funcionario WHERE id_pessoa = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id_pessoa);
            int affectedRows = pstmt.executeUpdate();

            // Verifica se alguma linha foi realmente deletada
            if(affectedRows == 0){
                throw new RuntimeException("Falha ao excluir: Funcionário com ID " + id_pessoa + " não encontrado.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar funcionário: " + e.getMessage(), e);
        }
    }

    /**
     * Busca um funcionário pelo seu ID (ID da pessoa).
     * @param id_pessoa ID do funcionário a buscar.
     * @return Objeto Funcionario se encontrado, senão null.
     */
    public Funcionario readById(int id_pessoa) {
        String sql = "SELECT p.id, p.nome, p.email, f.matricula, f.departamento " +
                "FROM pessoa p JOIN funcionario f ON p.id = f.id_pessoa WHERE f.id_pessoa = ?";
        Funcionario funcionario = null;
        try (Connection conn = Conexao.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id_pessoa);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
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
}
