package br.com.showdomilhao.dao;

import br.com.showdomilhao.connection.ConnectionFactory;
import br.com.showdomilhao.model.Difficulty;
import br.com.showdomilhao.util.LogUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Classe responsável por realizar operações de persistência relacionadas ao modelo {@link Difficulty} no banco de dados.
 */
public class DifficultyDAO {
    private final Connection connection;

    /**
     * Construtor que inicializa a conexão com o banco de dados.
     */
    public DifficultyDAO() {
        this.connection = ConnectionFactory.getConnection();
    }

    /**
     * Insere todas as dificuldades definidas na enumeração {@link Difficulty} na tabela <code>dificuldade</code> do banco de dados.
     * <p>
     * Para cada valor da enum {@link Difficulty}, este método configura os parâmetros do {@link PreparedStatement} e executa a atualização.
     * Em seguida, confirma a transação e registra a inserção bem-sucedida ou qualquer erro que ocorra.
     * </p>
     */
    public void saveAll() {
        String insertSql = "INSERT INTO dificuldade (id, nivel) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertSql)) {
            for (Difficulty value : Difficulty.values()) {
                stmt.setInt(1, value.getLevel());
                stmt.setString(2, value.getDescriptionPtBr());
                int rowsAffected = stmt.executeUpdate();
                connection.commit();
                LogUtils.getLogger(this).info("Inserted difficulty '{}' in the database, rows affected '{}'", value.name(), rowsAffected);
            }
        } catch (SQLException e) {
            LogUtils.getLogger(this).error("Error while trying to insert Difficulty", e);
        }
    }

    /**
     * Remove todas as entradas da tabela <code>dificuldade</code>.
     * <p>
     * Este método executa a atualização de deleção, confirma a transação e registra a deleção bem-sucedida ou qualquer erro que ocorra.
     * </p>
     */
    public void deleteAll() {
        String deleteSql = "DELETE FROM dificuldade";
        try (PreparedStatement stmt = connection.prepareStatement(deleteSql)) {
            int rowsAffected = stmt.executeUpdate();
            connection.commit();
            LogUtils.getLogger(this).info("Deleted difficulty from the database, rows affected '{}'", rowsAffected);
        } catch (SQLException e) {
            LogUtils.getLogger(this).error("Error while trying to delete difficulty", e);
        }
    }
}
