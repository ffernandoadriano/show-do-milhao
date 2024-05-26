package br.com.showdomilhao.application;

import br.com.showdomilhao.util.LogUtils;
import br.com.showdomilhao.util.StageAwareMedia;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URISyntaxException;
import java.util.Objects;

/**
 * Classe responsável por reproduzir arquivos de áudio MP3 continuamente, com opção de loop e controle de volume.
 */
public class ContinuousReproduction implements StageAwareMedia {
    private String fileMusic;
    private final boolean loop;
    private MediaPlayer mediaPlayer;
    private double volume = 1; // volume em 100%

    /**
     * Construtor que inicializa a reprodução contínua de áudio.
     *
     * @param fileMusic o caminho do arquivo de música a ser reproduzido
     * @param loop      indica se a reprodução deve ser contínua (loop)
     */
    public ContinuousReproduction(String fileMusic, boolean loop) {
        this.fileMusic = fileMusic;
        this.loop = loop;
    }

    /**
     * Construtor que inicializa a reprodução contínua de áudio sem especificar um arquivo.
     *
     * @param loop indica se a reprodução deve ser contínua (loop)
     */
    public ContinuousReproduction(boolean loop) {
        this(null, loop);
    }

    /**
     * Reproduz o som configurado.
     *
     * @return a instância atual de ContinuousReproduction para encadeamento de métodos
     */
    public ContinuousReproduction playSound() {
        try {
            if (mediaPlayer != null) {
                // pause a musica atual
                mediaPlayer.stop();
                // liberar os recursos associados a um MediaPlayer, para evitar vazamentos de recursos e garantir eficiencia.
                mediaPlayer.dispose();
            }

            new Thread(() -> {
                // cria uma nova media
                try {
                    mediaPlayer = new MediaPlayer(new Media(Objects.requireNonNull(getClass().getResource(fileMusic)).toURI().toString()));
                } catch (URISyntaxException e) {
                    LogUtils.getLogger(getClass()).error(e.getMessage(), e);
                }

                // reproduz a música
                mediaPlayer.play();

                // volume
                mediaPlayer.setVolume(volume);

                // Se a reprodução em loop estiver ativada, defina o evento de repetição
                // Quando a reprodução de uma mídia chega ao final, a posição de reprodução é definida de volta para o início, garantindo que a música seja repetida continuamente.
                if (loop) {
                    mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
                }

            }).start();

        } catch (Exception e) {
            LogUtils.getLogger(this).error(e.getMessage(), e);
        }

        return this;
    }

    /**
     * Configura o caminho do arquivo de música.
     *
     * @param fileMusic o caminho do arquivo de música a ser reproduzido
     */
    public void setFileMusic(String fileMusic) {
        this.fileMusic = fileMusic;
    }

    /**
     * Para a reprodução de som.
     */
    public void stopSound() {
        mediaPlayer.stop();
    }

    /**
     * Configura o volume da reprodução.
     *
     * @param value o valor do volume (0 a 1)
     * @return a instância atual de ContinuousReproduction para encadeamento de métodos
     */
    public ContinuousReproduction volume(double value) {
        this.volume = value;
        return this;
    }

    /**
     * Finaliza a thread paralela ao fechar o Stage.
     *
     * @param stage Stage associado.
     */
    @Override
    public void onStageDefined(Stage stage) {
        stage.setOnCloseRequest(event -> System.exit(0)
        );

    }
}
