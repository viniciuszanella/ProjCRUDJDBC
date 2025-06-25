package ProjCRUDJDBC.dao;

import ProjCRUDJDBC.db.Conexao;
import ProjCRUDJDBC.model.Pessoa;

import java.sql.*;

public class PessoaDAO {

    /**
     * Salva uma nova pessoa no banco de dados.
     * @param pessoa Objeto Pessoa a ser salvo.
     */
    public void create(Pessoa pessoa) {
        String sql = "INSERT INTO pessoa (nome, email) VALUES (?, ?)";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, pessoa.getNome());
            pstmt.setString(2, pessoa.getEmail());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            // Lança uma exceção mais específica para a camada de serviço/aplicação tratar
            throw new RuntimeException("Erro ao cadastrar pessoa: " + e.getMessage(), e);
        }
    }

    /**
     * Busca uma pessoa pelo seu ID.
     * @param id O ID da pessoa a ser buscada.
     * @return Um objeto Pessoa se encontrado, caso contrário, null.
     */
    public Pessoa readById(int id) {
        String sql = "SELECT * FROM pessoa WHERE id = ?";
        Pessoa pessoa = null;

        try (Connection conn = Conexao.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                pessoa = new Pessoa();
                pessoa.setId(rs.getInt("id"));
                pessoa.setNome(rs.getString("nome"));
                pessoa.setEmail(rs.getString("email"));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar pessoa por ID: " + e.getMessage(), e);
        }
        return pessoa;
    }
}
