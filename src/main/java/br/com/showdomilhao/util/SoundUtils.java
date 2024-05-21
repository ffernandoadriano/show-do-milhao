package br.com.showdomilhao.util;

import br.com.showdomilhao.application.ContinuousReproduction;

/**
 * Utilitários relacionados à reprodução de som na aplicação.
 */
public final class SoundUtils {

    private static boolean madeMillion = false;

    private static final ContinuousReproduction SOUND;

    static {
        SOUND = new ContinuousReproduction(true);
    }

    /**
     * Construtor privado para evitar a instanciação da classe
     */
    private SoundUtils() {
    }

    /**
     * Define e retorna uma instância de ContinuousReproduction para tocar música de abertura.
     *
     * @param pathOfMusic o caminho do arquivo de música
     * @return a instância de ContinuousReproduction configurada
     */
    public static ContinuousReproduction openingSound(String pathOfMusic) {
        SOUND.setFileMusic(pathOfMusic);
        return SOUND;
    }

    /**
     * Retorna o estado do jogador ter atingido um milhão.
     *
     * @return true se o jogador fez um milhão, false caso contrário
     */
    public static boolean isMadeMillion() {
        return madeMillion;
    }

    /**
     * Define o estado do jogador ter atingido um milhão.
     *
     * @param madeMillion o novo estado
     */
    public static void setMadeMillion(boolean madeMillion) {
        SoundUtils.madeMillion = madeMillion;
    }
}
