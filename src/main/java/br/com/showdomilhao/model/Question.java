package br.com.showdomilhao.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.StringJoiner;

/**
 * Classe que representa uma questão.
 * Cada questão possui um identificador, um nível de dificuldade, um enunciado,
 * três alternativas e uma resposta.
 */
public class Question {
    private final IntegerProperty id = new SimpleIntegerProperty(); // O identificador da questão
    private final ObjectProperty<Difficulty> difficulty = new SimpleObjectProperty<>(); // O nível de dificuldade da questão
    private final StringProperty enunciated = new SimpleStringProperty(); // O enunciado da questão
    private final StringProperty alternative1 = new SimpleStringProperty(); // Primeira alternativa da questão
    private final StringProperty alternative2 = new SimpleStringProperty(); // Segunda alternativa da questão
    private final StringProperty alternative3 = new SimpleStringProperty(); // Terceira alternativa da questão
    private final StringProperty response = new SimpleStringProperty(); // A resposta correta da questão

    /**
     * Obtém o identificador da questão.
     *
     * @return O identificador da questão
     */
    public int getId() {
        return id.get();
    }

    /**
     * Obtém a propriedade do identificador da questão.
     *
     * @return A propriedade do identificador da questão
     */
    public IntegerProperty idProperty() {
        return id;
    }

    /**
     * Define o identificador da questão.
     *
     * @param id O identificador da questão
     */
    public void setId(int id) {
        this.id.set(id);
    }

    /**
     * Obtém o nível de dificuldade da questão.
     *
     * @return O nível de dificuldade da questão
     */
    public Difficulty getDifficulty() {
        return difficulty.get();
    }

    /**
     * Obtém a propriedade do nível de dificuldade da questão.
     *
     * @return A propriedade do nível de dificuldade da questão
     */
    public ObjectProperty<Difficulty> difficultyProperty() {
        return difficulty;
    }

    /**
     * Define o nível de dificuldade da questão.
     *
     * @param difficulty O nível de dificuldade da questão
     */
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty.set(difficulty);
    }

    /**
     * Obtém o enunciado da questão.
     *
     * @return O enunciado da questão
     */
    public String getEnunciated() {
        return enunciated.get();
    }

    /**
     * Obtém a propriedade do enunciado da questão.
     *
     * @return A propriedade do enunciado da questão
     */
    public StringProperty enunciatedProperty() {
        return enunciated;
    }

    /**
     * Define o enunciado da questão.
     *
     * @param enunciated O enunciado da questão
     */
    public void setEnunciated(String enunciated) {
        this.enunciated.set(enunciated);
    }

    /**
     * Obtém a primeira alternativa da questão.
     *
     * @return A primeira alternativa da questão
     */
    public String getAlternative1() {
        return alternative1.get();
    }

    /**
     * Obtém a propriedade da primeira alternativa da questão.
     *
     * @return A propriedade da primeira alternativa da questão
     */
    public StringProperty alternative1Property() {
        return alternative1;
    }

    /**
     * Define a primeira alternativa da questão.
     *
     * @param alternative1 A primeira alternativa da questão
     */
    public void setAlternative1(String alternative1) {
        this.alternative1.set(alternative1);
    }

    /**
     * Obtém a segunda alternativa da questão.
     *
     * @return A segunda alternativa da questão
     */
    public String getAlternative2() {
        return alternative2.get();
    }

    /**
     * Obtém a propriedade da segunda alternativa da questão.
     *
     * @return A propriedade da segunda alternativa da questão
     */
    public StringProperty alternative2Property() {
        return alternative2;
    }

    /**
     * Define a segunda alternativa da questão.
     *
     * @param alternative2 A segunda alternativa da questão
     */
    public void setAlternative2(String alternative2) {
        this.alternative2.set(alternative2);
    }

    /**
     * Obtém a terceira alternativa da questão.
     *
     * @return A terceira alternativa da questão
     */
    public String getAlternative3() {
        return alternative3.get();
    }

    /**
     * Obtém a propriedade da terceira alternativa da questão.
     *
     * @return A propriedade da terceira alternativa da questão
     */
    public StringProperty alternative3Property() {
        return alternative3;
    }

    /**
     * Define a terceira alternativa da questão.
     *
     * @param alternative3 A terceira alternativa da questão
     */
    public void setAlternative3(String alternative3) {
        this.alternative3.set(alternative3);
    }

    /**
     * Obtém a resposta correta da questão.
     *
     * @return A resposta correta da questão
     */
    public String getResponse() {
        return response.get();
    }

    /**
     * Obtém a propriedade da resposta correta da questão.
     *
     * @return A propriedade da resposta correta da questão
     */
    public StringProperty responseProperty() {
        return response;
    }

    /**
     * Define a resposta correta da questão.
     *
     * @param response A resposta correta da questão
     */
    public void setResponse(String response) {
        this.response.set(response);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Question.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("difficulty=" + difficulty)
                .add("enunciated=" + enunciated)
                .add("alternative1=" + alternative1)
                .add("alternative2=" + alternative2)
                .add("alternative3=" + alternative3)
                .add("response=" + response)
                .toString();
    }
}
