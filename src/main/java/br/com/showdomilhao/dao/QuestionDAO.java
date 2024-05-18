package br.com.showdomilhao.dao;

import br.com.showdomilhao.connection.ConnectionFactory;
import br.com.showdomilhao.model.Difficulty;
import br.com.showdomilhao.model.Question;
import br.com.showdomilhao.util.LogUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Classe responsável por realizar operações de persistência relacionadas ao modelo {@link Question} no banco de dados.
 */
public class QuestionDAO {
    private final Connection connection;

    /**
     * Construtor que inicializa a conexão com o banco de dados.
     */
    public QuestionDAO() {
        this.connection = ConnectionFactory.getConnection();
    }

    /**
     * Salva uma nova pergunta no banco de dados.
     *
     * @param question a pergunta a ser salva
     * @return <code>true</code> se a operação for bem-sucedida, <code>false</code> caso contrário
     */
    public boolean save(Question question) {

        String insertSql = "INSERT INTO pergunta (id_nivel, enunciado, alternativa1, alternativa2, alternativa3, resposta) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(insertSql)) {
            stmt.setInt(1, question.getDifficulty().getLevel());
            stmt.setString(2, question.getEnunciated());
            stmt.setString(3, question.getAlternative1());
            stmt.setString(4, question.getAlternative2());
            stmt.setString(5, question.getAlternative3());
            stmt.setString(6, question.getResponse());
            int rowsAffected = stmt.executeUpdate();
            connection.commit();
            LogUtils.getLogger(this).info("Inserted question '{}' in the database, rows affected '{}'", question.getEnunciated(), rowsAffected);
            return true;

        } catch (SQLException e) {
            LogUtils.getLogger(this).error("Error while trying to insert question '{}'", question.getId(), e);
        }
        return false;
    }

    /**
     * Atualiza uma pergunta existente no banco de dados.
     *
     * @param question a pergunta a ser atualizada
     * @return <code>true</code> se a operação for bem-sucedida, <code>false</code> caso contrário
     */
    public boolean update(Question question) {
        String updateSql = "UPDATE pergunta SET id_nivel = ?, enunciado = ?, alternativa1 = ?, alternativa2 = ?, alternativa3 = ?, resposta = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(updateSql)) {
            stmt.setInt(1, question.getDifficulty().getLevel());
            stmt.setString(2, question.getEnunciated());
            stmt.setString(3, question.getAlternative1());
            stmt.setString(4, question.getAlternative2());
            stmt.setString(5, question.getAlternative3());
            stmt.setString(6, question.getResponse());
            stmt.setInt(7, question.getId());
            int rowsAffected = stmt.executeUpdate();
            connection.commit();
            LogUtils.getLogger(this).info("updated question '{}', rows affected '{}'", question.getId(), rowsAffected);
            return true;

        } catch (SQLException e) {
            LogUtils.getLogger(this).error("Error while trying to update question '{}'", question.getId(), e);
        }
        return false;
    }

    /**
     * Remove uma pergunta do banco de dados.
     *
     * @param id o ID da pergunta a ser removida
     * @return <code>true</code> se a operação for bem-sucedida, <code>false</code> caso contrário
     */
    public boolean delete(int id) {
        String deleteSql = "DELETE FROM pergunta WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteSql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            connection.commit();
            LogUtils.getLogger(this).info("Deleted question '{}' from the database, rows affected '{}'", id, rowsAffected);
            return true;

        } catch (SQLException e) {
            LogUtils.getLogger(this).error("Error while trying to delete question '{}'", id, e);
        }
        return false;
    }

    /**
     * Encontra todas as perguntas no banco de dados.
     *
     * @return uma lista de perguntas
     */
    public List<Question> findAll() {
        LogUtils.getLogger(this).info("Finding all questions");

        String querySql = "SELECT * FROM pergunta";
        return search(querySql, null);
    }

    /**
     * Encontra perguntas por nível de dificuldade.
     *
     * @param difficulty o nível de dificuldade das perguntas
     * @return uma lista de perguntas
     */
    public List<Question> findByLevel(Difficulty difficulty) {
        LogUtils.getLogger(this).info("Finding by level questions");

        String querySql = "SELECT * FROM pergunta WHERE id_nivel = ?";
        return search(querySql, difficulty);
    }

    /**
     * Encontra uma pergunta por nível de dificuldade, excluindo perguntas já feitas, e ordenando aleatoriamente.
     *
     * @param idsQuestionAsked IDs das perguntas que já foram feitas
     * @param difficulty       o nível de dificuldade das perguntas
     * @return a pergunta encontrada
     */
    public Question findByLevel(String idsQuestionAsked, Difficulty difficulty) {
        String querySql = "SELECT * FROM pergunta WHERE id_nivel = ? ORDER BY RANDOM() LIMIT 1";

        if (!idsQuestionAsked.isEmpty()) {
            querySql = String.format("SELECT * FROM pergunta WHERE id_nivel = ? AND id NOT IN %s ORDER BY RANDOM() LIMIT 1", idsQuestionAsked);
        }

        return search(querySql, difficulty).getFirst();
    }

    /**
     * Executa a busca de perguntas no banco de dados com base na consulta SQL fornecida.
     *
     * @param querySql   a consulta SQL a ser executada
     * @param difficulty o nível de dificuldade das perguntas, se aplicável
     * @return uma lista de perguntas
     */
    private List<Question> search(String querySql, Difficulty difficulty) {
        List<Question> questions = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(querySql)) {

            if (Objects.nonNull(difficulty)) {
                stmt.setInt(1, difficulty.getLevel());
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Question question = new Question();
                    question.setId(rs.getInt("id"));
                    question.setRow(rs.getRow()); // linha atual
                    question.setDifficulty(Difficulty.fromValue(rs.getInt("id_nivel")));
                    question.setEnunciated(rs.getString("enunciado"));
                    question.setAlternative1(rs.getString("alternativa1"));
                    question.setAlternative2(rs.getString("alternativa2"));
                    question.setAlternative3(rs.getString("alternativa3"));
                    question.setResponse(rs.getString("resposta"));

                    questions.add(question);
                }
            }

        } catch (SQLException e) {
            LogUtils.getLogger(this).error("Error while trying to search", e);
        }

        return questions;
    }

}
