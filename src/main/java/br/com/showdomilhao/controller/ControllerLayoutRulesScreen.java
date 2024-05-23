package br.com.showdomilhao.controller;

import br.com.showdomilhao.util.FXUtils;
import br.com.showdomilhao.util.RulesUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 * Controlador para a tela de regras do layout do Show do Milhão.
 * <p>
 * Esta classe é responsável por inicializar a tela de regras e fornecer
 * funcionalidades para retornar à tela inicial ou fechar a aplicação.
 */
public class ControllerLayoutRulesScreen {

    /**
     * Label para exibir as regras do jogo.
     */
    @FXML
    private Label lblRules;

    /**
     * Método de inicialização chamado automaticamente após o carregamento do layout FXML.
     * <p>
     * Define o texto do {@code lblRules} com as regras obtidas de {@link RulesUtils#getRules()}.
     */
    @FXML
    private void initialize() {
        lblRules.setText(RulesUtils.getRules());
    }

    /**
     * Manipulador de evento para retornar à tela inicial.
     * <p>
     * Chama {@link FXUtils#changeLayout(Class, String)} para alterar o layout para a tela inicial.
     */
    @FXML
    public void goBack() {
        // Altera o layout
        FXUtils.changeLayout(getClass(), "/view/LayoutHomeScreen.fxml");
    }

    /**
     * Manipulador de evento para fechar a aplicação.
     * <p>
     * Reproduz um áudio de despedida e, após um atraso de 2 segundos, encerra a aplicação.
     */
    @FXML
    public void close() {
        FXUtils.changeVoice("/songs/tchau-pessoal-até-a-proxima-vez-voice.mp3");
        FXUtils.setDelayTransition(Duration.seconds(2), event -> FXUtils.exit());
    }
}
