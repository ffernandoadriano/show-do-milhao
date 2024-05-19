package br.com.showdomilhao.service;

import br.com.showdomilhao.dao.DifficultyDAO;
import br.com.showdomilhao.model.Difficulty;

/**
 * Classe responsável por fornecer serviços relacionados ao modelo {@link Difficulty}.
 */
public class DifficultyService {
    private final DifficultyDAO difficultyDAO;

    /**
     * Construtor que inicializa o serviço de dificuldade com uma instância de {@link DifficultyDAO}.
     */
    public DifficultyService() {
        difficultyDAO = new DifficultyDAO();
    }

    /**
     * Salva todas as dificuldades no banco de dados.
     */
    public void saveAll() {
        difficultyDAO.saveAll();
    }

    /**
     * Deleta todas as dificuldades no banco de dados.
     */
    public void deleteAll() {
        difficultyDAO.deleteAll();
    }
}
