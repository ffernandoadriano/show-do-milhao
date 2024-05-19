package br.com.showdomilhao.dao;

import br.com.showdomilhao.connection.ConnectionFactory;
import br.com.showdomilhao.model.Player;
import br.com.showdomilhao.util.LogUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por realizar operações de persistência relacionadas ao modelo {@link Player} no banco de dados.
 */
public class PlayerDAO {
    private final Connection connection;

    /**
     * Construtor que inicializa a conexão com o banco de dados.
     */
    public PlayerDAO() {
        connection = ConnectionFactory.getConnection();
    }

    /**
     * Salva um novo jogador no banco de dados.
     *
     * @param player o jogador a ser salvo
     */
    public void save(Player player) {
        String insertSql = "INSERT INTO jogador (nome) VALUES (?)";

        // Criar uma instrução SQL para inserção com retorno do ID gerado
        try (PreparedStatement stmt = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, player.getName());

            // Executar a inserção
            int rowsAffected = stmt.executeUpdate();

            // Obter o ID gerado pelo banco de dados
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    // SQLite, o método getGeneratedKeys() pode não retornar as colunas pelo nome, o que é uma limitação específica dessa implementação.
                    int generatedId = rs.getInt(1);
                    // Insere o ID na referência do objeto.
                    player.setId(generatedId);
                }
            }

            // efetiva no banco
            connection.commit();
            LogUtils.getLogger(this).info("Inserted player '{}' in the database, rows affected '{}'", player.getName(), rowsAffected);

        } catch (SQLException e) {
            LogUtils.getLogger(this).error("Error while trying to insert player '{}'", player.getName(), e);
        }
    }

    /**
     * Atualiza um jogador existente no banco de dados.
     *
     * @param player o jogador a ser atualizado
     */
    public void update(Player player) {
        String updateSql = "UPDATE jogador SET nome = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(updateSql)) {
            stmt.setString(1, player.getName());
            stmt.setInt(2, player.getId());
            int rowsAffected = stmt.executeUpdate();
            connection.commit();
            LogUtils.getLogger(this).info("updated player '{}', rows affected '{}'", player.getId(), rowsAffected);
        } catch (SQLException e) {
            LogUtils.getLogger(this).error("Error while trying to update player '{}'", player.getId(), e);
        }

    }

    /**
     * Encontra todos os jogadores no banco de dados.
     *
     * @return uma lista de jogadores
     */
    public List<Player> findAll() {
        LogUtils.getLogger(this).info("Finding all players");

        String querySql = "SELECT * FROM jogador";
        List<Player> players = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(querySql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Player player = new Player();

                player.setId(rs.getInt("id"));
                player.setName(rs.getString("nome"));

                players.add(player);
            }
        } catch (SQLException e) {
            LogUtils.getLogger(this).error("Error while trying to find all players", e);
        }

        return players;
    }

    /**
     * Encontra um jogador pelo nome no banco de dados.
     *
     * @param name o nome do jogador a ser encontrado
     * @return o jogador encontrado, ou <code>null</code> se não for encontrado
     */
    public Player findByName(String name) {
        LogUtils.getLogger(this).info("Finding player by name");

        String querySql = "SELECT * FROM jogador WHERE UPPER(nome) = ?";

        try (PreparedStatement stmt = connection.prepareStatement(querySql)) {
            stmt.setString(1, name.toUpperCase());

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    Player player = new Player();
                    player.setId(rs.getInt("id"));
                    player.setName(rs.getString("nome"));

                    LogUtils.getLogger(this).info("Player '{}' found", player.getName());

                    return player;
                }

            }

        } catch (SQLException e) {
            LogUtils.getLogger(this).error("Error while trying to find by name player");
        }

        LogUtils.getLogger(this).info("Player not found");
        return null;
    }
}
