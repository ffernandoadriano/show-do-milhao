package br.com.showdomilhao.controller;

import br.com.showdomilhao.util.FXUtils;
import javafx.fxml.FXML;
import javafx.util.Duration;

/**
 * Controlador para a tela inicial do layout do Show do Milhão.
 * <p>
 * Esta classe é responsável por gerenciar as ações na tela inicial,
 * incluindo navegação para outras telas do aplicativo.
 */
public class ControllerLayoutHomeScreen {

    /**
     * Manipulador de evento para iniciar o jogo.
     * <p>
     * Altera o layout para a tela de entrada do nome do jogador.
     */
    @FXML
    public void onPlay() {
        // Altera o layout
        FXUtils.changeLayout(getClass(), "/view/LayoutNameScreen.fxml");
    }

    /**
     * Manipulador de evento para visualizar as regras do jogo.
     * <p>
     * Altera o layout para a tela de regras.
     */
    @FXML
    public void onRules() {
        // Altera o layout
        FXUtils.changeLayout(getClass(), "/view/LayoutRulesScreen.fxml");
    }

    /**
     * Manipulador de evento para visualizar o ranking.
     * <p>
     * Altera o layout para a tela de ranking.
     */
    @FXML
    public void onRanking() {
        // Altera o layout
        FXUtils.changeLayout(getClass(), "/view/LayoutRankingScreen.fxml");
    }

    /**
     * Manipulador de evento para gerenciar as perguntas.
     * <p>
     * Altera o layout para a tela de gerenciamento de perguntas.
     */
    @FXML
    public void onQuestion() {
        // Altera o layout
        FXUtils.changeLayout(getClass(), "/view/LayoutQuestionScreen.fxml");
    }

    /**
     * Manipulador de evento para fechar a aplicação.
     * <p>
     * Toca um áudio de despedida e, após um atraso de 2 segundos, encerra a aplicação.
     */
    @FXML
    public void onClose() {
        FXUtils.changeVoice("/songs/tchau-pessoal-até-a-proxima-vez-voice.mp3");
        FXUtils.setDelayTransition(Duration.seconds(2), event -> FXUtils.exit());
    }

}
