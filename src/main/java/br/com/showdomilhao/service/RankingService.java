package br.com.showdomilhao.service;

import br.com.showdomilhao.dao.RankingDAO;
import br.com.showdomilhao.model.Ranking;

import java.util.List;

/**
 * Classe responsável por fornecer serviços relacionados ao modelo {@link Ranking}.
 */
public class RankingService {
    private final RankingDAO rankingDAO;

    /**
     * Construtor que inicializa o serviço de ranking com uma instância de {@link RankingDAO}.
     */
    public RankingService() {
        rankingDAO = new RankingDAO();
    }

    /**
     * Salva um novo ranking no banco de dados.
     *
     * @param ranking o ranking a ser salvo
     */
    public void save(Ranking ranking) {
        rankingDAO.save(ranking);
    }

    /**
     * Atualiza um ranking existente no banco de dados.
     *
     * @param ranking o ranking a ser atualizado
     */
    public void update(Ranking ranking) {
        rankingDAO.update(ranking);
    }

    /**
     * Encontra todos os rankings no banco de dados ordenados por pontuação.
     *
     * @return uma lista de rankings
     */
    public List<Ranking> findAllByRanking() {
        return rankingDAO.findAllByRanking();
    }

    /**
     * Deleta todos os rankings do banco de dados.
     */
    public void deleteAllRanking() {
        rankingDAO.deleteAllRanking();
    }

    /**
     * Encontra um ranking pelo ID no banco de dados.
     *
     * @param id o ID do ranking a ser encontrado
     * @return o ranking encontrado, ou null se não for encontrado
     */
    public Ranking findById(int id) {
        return rankingDAO.findByID(id);
    }

}
