package br.com.showdomilhao.service;

import br.com.showdomilhao.dao.PlayerDAO;
import br.com.showdomilhao.model.Player;

import java.util.List;
import java.util.Objects;

/**
 * Classe responsável por fornecer serviços relacionados ao modelo {@link Player}.
 */
public class PlayerService {

    private final PlayerDAO playerDAO;

    /**
     * Construtor que inicializa o serviço de jogador com uma instância de {@link PlayerDAO}.
     */
    public PlayerService() {
        playerDAO = new PlayerDAO();
    }

    /**
     * Insere um novo jogador no banco de dados, caso contrário, adiciona o id e o nome do jogador já criado
     * à referência do objeto.
     *
     * @param player o jogador a ser salvo
     */

    public void save(Player player) {
        // Verifica se o nome do jogador já existe no banco de dados.
        Player filteredPlayer = findByName(player.getName());

        if (Objects.nonNull(filteredPlayer)) {
            // insere o id ao objeto
            player.setId(filteredPlayer.getId());
            // Retornar o nome que foi inserido no banco de dados para manter a consistência
            player.setName(filteredPlayer.getName());
        } else {
            playerDAO.save(player);
        }
    }

    /**
     * Atualiza um jogador existente no banco de dados.
     *
     * @param player o jogador a ser atualizado
     */
    public void update(Player player) {
        playerDAO.update(player);
    }

    /**
     * Encontra todos os jogadores no banco de dados.
     *
     * @return uma lista de jogadores
     */
    public List<Player> findAll() {
        return playerDAO.findAll();
    }

    /**
     * Encontra um jogador pelo nome no banco de dados.
     *
     * @param name o nome do jogador a ser encontrado
     * @return o jogador encontrado, ou <code>null</code> se não for encontrado
     */
    public Player findByName(String name) {
        return playerDAO.findByName(name);
    }

}
