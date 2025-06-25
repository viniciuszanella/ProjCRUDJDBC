package ProjCRUDJDBC.dao;

import ProjCRUDJDBC.db.Conexao;
import ProjCRUDJDBC.model.Projeto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProjetoDAO {

    /**
     * Cria um novo projeto, validando a existência do funcionário responsável.
     * REGRA DE NEGÓCIO 2: Um projeto não pode ser criado sem vínculo com um funcionário existente.
     * @param projeto O projeto a ser criado.
     */
    public void create(Projeto projeto) {
        // Regra de Negócio 2: Verificar se o Funcionário responsável existe
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
        if (funcionarioDAO.readById(projeto.getId_funcionario()) == null) {
            // Regra de Negócio 4: Mensagem de erro clara
            throw new RuntimeException("Falha ao criar projeto: Funcionário responsável com ID " + projeto.getId_funcionario() + " não existe.");
        }

        String sql = "INSERT INTO projeto (nome, descricao, id_funcionario) VALUES (?, ?, ?)";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, projeto.getNome());
            pstmt.setString(2, projeto.getDescricao());
            pstmt.setInt(3, projeto.getId_funcionario());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir projeto: " + e.getMessage(), e);
        }
    }

    /**
     * Conta quantos projetos estão associados a um determinado funcionário.
     * Método auxiliar para a Regra de Negócio 3 (proibir exclusão de funcionário).
     * @param id_funcionario O ID do funcionário.
     * @return O número de projetos vinculados.
     */
    public int countProjetosByFuncionario(int id_funcionario) {
        String sql = "SELECT COUNT(*) FROM projeto WHERE id_funcionario = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id_funcionario);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar projetos do funcionário: " + e.getMessage(), e);
        }
        return 0;
    }
}
