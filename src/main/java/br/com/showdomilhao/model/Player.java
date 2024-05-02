package br.com.showdomilhao.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * Classe que representa um jogador.
 * Esta classe encapsula os dados de um jogador, incluindo seu identificador e nome.
 */
public class Player {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty name = new SimpleStringProperty();

    /**
     * Obtém o identificador do jogador.
     *
     * @return O identificador do jogador.
     */
    public int getId() {
        return id.get();
    }

    /**
     * Obtém a propriedade do identificador do jogador.
     *
     * @return A propriedade do identificador do jogador.
     */
    public IntegerProperty idProperty() {
        return id;
    }

    /**
     * Define o identificador do jogador.
     *
     * @param id O identificador a ser definido para o jogador.
     */
    public void setId(int id) {
        this.id.set(id);
    }

    /**
     * Obtém o nome do jogador.
     *
     * @return O nome do jogador.
     */
    public String getName() {
        return name.get();
    }

    /**
     * Obtém a propriedade do nome do jogador.
     *
     * @return A propriedade do nome do jogador.
     */
    public StringProperty nameProperty() {
        return name;
    }

    /**
     * Define o nome do jogador.
     *
     * @param name O nome a ser definido para o jogador.
     */
    public void setName(String name) {
        this.name.set(name);
    }

    /**
     * Retorna uma representação em string do objeto jogador.
     *
     * @return Uma string que representa o jogador.
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", Player.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name=" + name)
                .toString();
    }

    /**
     * Verifica se o objeto fornecido é igual a este jogador.
     * Dois jogadores são considerados iguais se possuírem o mesmo nome.
     *
     * @param o O objeto a ser comparado com este jogador.
     * @return true se os objetos forem iguais, false caso contrário.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;
        return Objects.equals(name, player.name);
    }

    /**
     * Retorna o código hash para este jogador.
     * O código hash é calculado com base no nome do jogador.
     *
     * @return O código hash para este jogador.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
