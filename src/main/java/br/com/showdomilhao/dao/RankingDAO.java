package br.com.showdomilhao.dao;

import br.com.showdomilhao.connection.ConnectionFactory;
import br.com.showdomilhao.model.Player;
import br.com.showdomilhao.model.Ranking;
import br.com.showdomilhao.util.LogUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por realizar operações de persistência relacionadas ao modelo {@link Ranking} no banco de dados.
 */
public class RankingDAO {
    private final Connection connection;

    /**
     * Construtor que inicializa a conexão com o banco de dados.
     */
    public RankingDAO() {
        connection = ConnectionFactory.getConnection();
    }

    /**
     * Salva um novo ranking no banco de dados.
     *
     * @param ranking o ranking a ser salvo
     * @return <code>true</code> se a operação for bem-sucedida, <code>false</code> caso contrário
     */
    public boolean save(Ranking ranking) {
        String insertSql = "INSERT INTO ranking (id, pontuacao) VALUES (?,?)";

        try (PreparedStatement stmt = connection.prepareStatement(insertSql)) {
            stmt.setInt(1, ranking.getPlayer().getId());
            stmt.setInt(2, ranking.getPunctuation());
            int rowsAffected = stmt.executeUpdate();
            connection.commit();
            LogUtils.getLogger(this).info("Inserted player ranking '{}' in the database, rows affected '{}'", ranking.getPlayer().getName(), rowsAffected);
            return true;

        } catch (SQLException e) {
            LogUtils.getLogger(this).error("Error while trying to insert player ranking '{}'", ranking.getPlayer().getName(), e);
        }

        return false;
    }

    /**
     * Atualiza um ranking existente no banco de dados.
     *
     * @param ranking o ranking a ser atualizado
     */
    public void update(Ranking ranking) {
        String updateSql = "UPDATE ranking SET pontuacao = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(updateSql)) {
            stmt.setInt(1, ranking.getPunctuation());
            stmt.setInt(2, ranking.getPlayer().getId());
            int rowsAffected = stmt.executeUpdate();
            connection.commit();
            LogUtils.getLogger(this).info("updated player ranking '{}', rows affected '{}'", ranking.getPlayer().getId(), rowsAffected);

        } catch (SQLException e) {
            LogUtils.getLogger(this).error("Error while trying to update player ranking '{}'", ranking.getPlayer().getId(), e);
        }

    }

    /**
     * Encontra todos os rankings, ordenados por pontuação em ordem decrescente, limitados aos 10 melhores.
     *
     * @return uma lista de rankings
     */
    public List<Ranking> findAllByRanking() {
        LogUtils.getLogger(this).info("Finding all by ranking");

        String querySql = "SELECT * FROM jogador JOIN ranking  USING(ID) ORDER BY pontuacao DESC LIMIT 10";

        List<Ranking> rankings = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(querySql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Ranking ranking = new Ranking();
                ranking.setPlayer(findPlayerById(rs.getInt("id")));
                ranking.setRank(rs.getRow()); // linha atual
                ranking.setPunctuation(rs.getInt("pontuacao"));

                rankings.add(ranking);
            }
        } catch (SQLException e) {
            LogUtils.getLogger(this).error("Error while trying to find all by ranking", e);
        }

        return rankings;
    }

    /**
     * Encontra um jogador pelo seu ID.
     *
     * @param id o ID do jogador
     * @return o jogador encontrado
     */
    private Player findPlayerById(int id) {
        String querySql = "SELECT * FROM jogador WHERE id = ?";

        Player player = new Player();

        try (PreparedStatement stmt = connection.prepareStatement(querySql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    player.setId(rs.getInt("id"));
                    player.setName(rs.getString("nome"));
                }
            }

        } catch (SQLException e) {
            LogUtils.getLogger(this).error("Error while trying to find by id", e);
        }

        return player;
    }

    /**
     * Encontra um ranking pelo seu ID.
     *
     * @param id o ID do ranking
     * @return o ranking encontrado, ou <code>null</code> se não encontrado
     */
    public Ranking findByID(int id) {
        LogUtils.getLogger(this).info("Finding ranking by id");

        String querySql = "SELECT * FROM ranking WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(querySql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    Ranking ranking = new Ranking();
                    ranking.setPlayer(findPlayerById(rs.getInt("id")));
                    ranking.setPunctuation(rs.getInt("pontuacao"));

                    LogUtils.getLogger(this).info("Ranking '{}' found", ranking.getPlayer().getName());

                    return ranking;
                }
            }

        } catch (SQLException e) {
            LogUtils.getLogger(this).error("Error while trying to find all by ranking", e);
        }
        return null;
    }

    /**
     * Remove todos os rankings do banco de dados.
     */
    public void deleteAllRanking() {
        // realiza exclusão de todos os jogadores vinculados ao ranking.
        // TRG_DELETE_PLAYER é acionada
        String deleteSql = "DELETE FROM ranking";

        try (PreparedStatement stmt = connection.prepareStatement(deleteSql)) {
            int rowsAffected = stmt.executeUpdate();
            connection.commit();
            LogUtils.getLogger(this).info("Deleted ranking and player from the database, rows affected '{}'", rowsAffected);

        } catch (SQLException e) {
            LogUtils.getLogger(this).error("Error while trying to delete ranking", e);
        }
    }


}
