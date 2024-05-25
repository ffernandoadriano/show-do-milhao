package br.com.showdomilhao.controller;

import br.com.showdomilhao.util.FXUtils;
import br.com.showdomilhao.util.LogUtils;
import br.com.showdomilhao.util.SoundUtils;
import javafx.fxml.FXML;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.net.URISyntaxException;
import java.util.Objects;

/**
 * Controlador para a tela de parabéns do Show do Milhão.
 * <p>
 * Esta classe é responsável por gerenciar a reprodução do vídeo de parabéns
 * e pela navegação para a tela de ranking após o término do vídeo.
 */
public class ControllerLayoutCongratulationScreen {
    @FXML
    private MediaView mediaView;

    /**
     * Método de inicialização chamado pelo JavaFX após a injeção dos componentes FXML.
     * <p>
     * Este método configura a reprodução do vídeo de parabéns e define o comportamento
     * ao final do vídeo, que é navegar para a tela de ranking.
     */
    @FXML
    private void initialize() {
        try {
            Media media = new Media(Objects.requireNonNull(getClass().getResource("/video/encerramento-show-do-milhao.mp4")).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            mediaPlayer.setAutoPlay(true);

            mediaPlayer.setOnEndOfMedia(() -> FXUtils.changeLayout(getClass(), "/view/LayoutRankingScreen.fxml"));

            // ganhou um milhão
            SoundUtils.setMadeMillion(true);

        } catch (URISyntaxException e) {
            LogUtils.getLogger(getClass()).error(e.getMessage());
        }

    }
}
