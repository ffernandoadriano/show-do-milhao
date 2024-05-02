package br.com.showdomilhao.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.StringJoiner;

/**
 * Classe que representa um ranking de jogadores.
 * Esta classe armazena informações sobre a classificação, pontuação e jogador associado a um ranking específico.
 */
public class Ranking {

    private final ObjectProperty<Player> player = new SimpleObjectProperty<>();
    private final IntegerProperty rank = new SimpleIntegerProperty();
    private final IntegerProperty punctuation = new SimpleIntegerProperty();

    /**
     * Obtém o jogador associado ao ranking.
     *
     * @return O jogador associado ao ranking.
     */
    public Player getPlayer() {
        return player.get();
    }

    /**
     * Obtém a propriedade do jogador associado ao ranking.
     *
     * @return A propriedade do jogador associado ao ranking.
     */
    public ObjectProperty<Player> playerProperty() {
        return player;
    }

    /**
     * Define o jogador associado ao ranking.
     *
     * @param player O jogador a ser associado ao ranking.
     */
    public void setPlayer(Player player) {
        this.player.set(player);
    }

    /**
     * Obtém a pontuação do jogador no ranking.
     *
     * @return A pontuação do jogador no ranking.
     */
    public int getPunctuation() {
        return punctuation.get();
    }

    /**
     * Obtém a propriedade da pontuação do jogador no ranking.
     *
     * @return A propriedade da pontuação do jogador no ranking.
     */
    public IntegerProperty punctuationProperty() {
        return punctuation;
    }

    /**
     * Define a pontuação do jogador no ranking.
     *
     * @param punctuation A pontuação a ser definida para o jogador no ranking.
     */
    public void setPunctuation(int punctuation) {
        this.punctuation.set(punctuation);
    }

    /**
     * Obtém o nome do jogador associado ao ranking.
     *
     * @return O nome do jogador associado ao ranking.
     */
    public String getPlayerName() {
        return player.get().getName();
    }

    /**
     * Obtém a classificação do jogador no ranking.
     *
     * @return A classificação do jogador no ranking.
     */
    public int getRank() {
        return rank.get();
    }

    /**
     * Obtém a propriedade da classificação do jogador no ranking.
     *
     * @return A propriedade da classificação do jogador no ranking.
     */
    public IntegerProperty rankProperty() {
        return rank;
    }

    /**
     * Define a classificação do jogador no ranking.
     *
     * @param rank A classificação a ser definida para o jogador no ranking.
     */
    public void setRank(int rank) {
        this.rank.set(rank);
    }

    /**
     * Retorna uma representação em string do objeto ranking.
     *
     * @return Uma string que representa o ranking.
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", Ranking.class.getSimpleName() + "[", "]")
                .add("player=" + player)
                .add("punctuation=" + punctuation)
                .toString();
    }
}