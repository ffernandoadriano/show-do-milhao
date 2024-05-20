package br.com.showdomilhao.service;

import br.com.showdomilhao.dao.QuestionDAO;
import br.com.showdomilhao.model.Difficulty;
import br.com.showdomilhao.model.Question;
import br.com.showdomilhao.util.FXUtils;

import java.util.List;

/**
 * Classe responsável por fornecer serviços relacionados ao modelo {@link Question}.
 */
public class QuestionService {
    private final QuestionDAO questionDAO;

    /**
     * Construtor que inicializa o serviço de pergunta com uma instância de {@link QuestionDAO}.
     */
    public QuestionService() {
        questionDAO = new QuestionDAO();
    }

    /**
     * Salva uma nova pergunta no banco de dados e exibe uma confirmação.
     *
     * @param question a pergunta a ser salva
     */
    public void save(Question question) {
        if (questionDAO.save(question)) {
            FXUtils.showConfirmDialogService("Pergunta adicionada com sucesso!");
        }
    }

    /**
     * Atualiza uma pergunta existente no banco de dados e exibe uma confirmação.
     *
     * @param question a pergunta a ser atualizada
     */
    public void update(Question question) {
        if (questionDAO.update(question)) {
            FXUtils.showConfirmDialogService("Alteração realizada com sucesso!");
        }
    }

    /**
     * Deleta uma pergunta do banco de dados pelo ID e exibe uma confirmação.
     *
     * @param id o ID da pergunta a ser deletada
     */
    public void delete(int id) {
        if (questionDAO.delete(id)) {
            FXUtils.showConfirmDialogService("Pergunta removida com sucesso!");
        }
    }

    /**
     * Encontra todas as perguntas no banco de dados.
     *
     * @return uma lista de perguntas
     */
    public List<Question> findAll() {
        return questionDAO.findAll();
    }

    /**
     * Encontra perguntas pelo nível de dificuldade no banco de dados.
     *
     * @param difficulty o nível de dificuldade das perguntas a serem encontradas
     * @return uma lista de perguntas filtradas pelo nível de dificuldade
     */
    public List<Question> findByLevel(Difficulty difficulty) {
        return questionDAO.findByLevel(difficulty);
    }

    /**
     * Encontra uma pergunta aleatória pelo nível de dificuldade, excluindo perguntas já feitas.
     *
     * @param idQuestionAsked IDs das perguntas que já foram feitas
     * @param difficulty      o nível de dificuldade da pergunta a ser encontrada
     * @return uma pergunta aleatória filtrada pelo nível de dificuldade
     */
    public Question findByLevel(String idQuestionAsked, Difficulty difficulty) {
        return questionDAO.findByLevel(idQuestionAsked, difficulty);
    }
}
