package ProjCRUDJDBC.dao;

import ProjCRUDJDBC.db.Conexao;
import ProjCRUDJDBC.model.Projeto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjetoDAO {

    public void create(Projeto projeto) {
        // Validação da regra de negócio: o funcionário responsável deve existir.
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
        if (funcionarioDAO.readById(projeto.getId_funcionario()) == null) {
            throw new RuntimeException("Falha ao criar projeto: Funcionário responsável com ID " + projeto.getId_funcionario() + " não existe.");
        }

        // Comando SQL para inserir um novo registro na tabela 'projeto'.
        String sql = "INSERT INTO projeto (nome, descricao, id_funcionario) VALUES (?, ?, ?)";

        // Utiliza try-with-resources para garantir que a conexão e o statement sejam fechados.
        try (Connection conn = Conexao.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Define os parâmetros da consulta SQL com os dados do objeto projeto.
            pstmt.setString(1, projeto.getNome());
            pstmt.setString(2, projeto.getDescricao());
            pstmt.setInt(3, projeto.getId_funcionario());

            // Executa a inserção no banco de dados.
            pstmt.executeUpdate();

        } catch (SQLException e) {
            // Captura exceções SQL e as relança como RuntimeException para a camada de serviço.
            throw new RuntimeException("Erro ao inserir projeto: " + e.getMessage(), e);
        }
    }

    public int countProjetosByFuncionario(int id_funcionario) {
        // Consulta que conta (`COUNT(*)`) as linhas na tabela 'projeto' onde a coluna 'id_funcionario' corresponde ao ID fornecido.
        String sql = "SELECT COUNT(*) FROM projeto WHERE id_funcionario = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id_funcionario);
            ResultSet rs = pstmt.executeQuery();

            // Se a consulta retornar um resultado, retorna o valor da primeira coluna (que é o resultado do COUNT).
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar projetos do funcionário: " + e.getMessage(), e);
        }
        // Retorna 0 se nenhum projeto for encontrado ou em caso de erro na consulta.
        return 0;
    }

    public List<Projeto> findAll() {
        // Inicializa uma lista vazia para armazenar os projetos.
        List<Projeto> projetos = new ArrayList<>();
        // Consulta para selecionar todos os campos de todos os projetos, ordenados pelo ID.
        String sql = "SELECT id, nome, descricao, id_funcionario FROM projeto ORDER BY id";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Itera sobre cada linha do resultado da consulta.
            while (rs.next()) {
                // Cria um novo objeto Projeto para cada linha retornada e o popula com os dados.
                Projeto projeto = new Projeto(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("descricao"),
                        rs.getInt("id_funcionario")
                );
                // Adiciona o objeto projeto à lista.
                projetos.add(projeto);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Falha ao buscar projetos no banco de dados.", e);
        }
        return projetos;
    }

    public void delete(int id) {
        // Comando SQL para deletar um registro da tabela 'projeto' com base no ID.
        String sql = "DELETE FROM projeto WHERE id = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            // Executa a exclusão e armazena o número de linhas afetadas.
            int rowsAffected = stmt.executeUpdate();

            // Verifica se alguma linha foi de fato excluída. Se não, informa ao usuário com um aviso.
            if (rowsAffected == 0) {
                System.out.println("AVISO: Nenhum projeto encontrado com o ID " + id + " para excluir.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Falha ao excluir projeto do banco de dados.", e);
        }
    }
}