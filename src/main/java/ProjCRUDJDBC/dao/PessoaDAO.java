package ProjCRUDJDBC.dao;

import ProjCRUDJDBC.db.Conexao;
import ProjCRUDJDBC.model.Pessoa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) para a entidade Pessoa.
 * Contém os métodos para interagir com a tabela 'pessoa' no banco de dados.
 */
public class PessoaDAO {

    /**
     * Insere uma nova pessoa no banco de dados.
     * O ID da pessoa é gerado automaticamente pelo banco.
     * @param pessoa Objeto Pessoa contendo os dados a serem salvos.
     */
    public void create(Pessoa pessoa) {
        String sql = "INSERT INTO pessoa (nome, email) VALUES (?, ?)";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, pessoa.getNome());
            pstmt.setString(2, pessoa.getEmail());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar pessoa: " + e.getMessage(), e);
        }
    }

    /**
     * Busca uma pessoa no banco de dados pelo seu ID.
     * @param id O ID da pessoa a ser buscada.
     * @return Um objeto Pessoa se encontrado, caso contrário, retorna null.
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

    /**
     * Retorna uma lista com todas as pessoas cadastradas no banco de dados.
     * @return Uma lista de objetos Pessoa.
     */
    public List<Pessoa> findAll() {
        List<Pessoa> pessoas = new ArrayList<>();
        String sql = "SELECT id, nome, email FROM pessoa ORDER BY id";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Pessoa pessoa = new Pessoa(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("email")
                );
                pessoas.add(pessoa);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Falha ao buscar pessoas no banco de dados.", e);
        }
        return pessoas;
    }
}