package br.com.showdomilhao.application;

import br.com.showdomilhao.util.LogUtils;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * Classe responsável por reproduzir arquivos de áudio MP3 em uma thread separada.
 */
public class Audio extends Thread {

    private String mp3Path;

    /**
     * Método para tocar um arquivo de áudio MP3.
     *
     * @param mp3 o caminho do arquivo MP3 a ser reproduzido
     */
    public void tocar(String mp3) {
        mp3Path = mp3;

        // inicia a thread ao chamar o método
        this.start();
    }

    /**
     * Executa a thread que lê e reproduz o áudio MP3.
     */
    @Override
    public void run() {
        try {
            Media media = new Media(Objects.requireNonNull(getClass().getResource(mp3Path)).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);

            // reproduz a música
            mediaPlayer.play();

            // espera a música terminar
            await(mediaPlayer);

            // liberar os recursos associados a um MediaPlayer, para evitar vazamentos de recursos e garantir eficiencia.
            mediaPlayer.dispose();
        } catch (Exception e) {
            LogUtils.getLogger(this).error(null, e);
        }
    }

    /**
     * Aguarda até que o MediaPlayer termine de reproduzir o áudio.
     *
     * @param mediaPlayer o MediaPlayer que está reproduzindo o áudio
     */
    private void await(MediaPlayer mediaPlayer) {
        // Configurando o CountDownLatch com 1
        CountDownLatch latch = new CountDownLatch(1);

        // Quando a música terminar de tocar, decrementamos o latch
        mediaPlayer.setOnEndOfMedia(latch::countDown);

        try {
            // Faz com que o thread atual espere até que a contagem regressiva da trava chegue a zero
            latch.await();
        } catch (InterruptedException e) {
            LogUtils.getLogger(this).info(null, e);
            // Limpe tudo o que precisa ser tratado antes de interromper
            Thread.currentThread().interrupt();
        }
    }
}
