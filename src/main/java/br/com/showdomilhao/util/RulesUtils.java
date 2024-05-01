package br.com.showdomilhao.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Classe utilitária para recuperar regras de um arquivo.
 */
public final class RulesUtils {

    // Caminho para o arquivo de regras
    private static final Path RULES_FILE = Path.of("src/main/resources/rules/regras.txt");

    // Variável de string para armazenar as regras
    private static String rules;

    // Bloco de inicialização estático para ler as regras do arquivo
    static {
        try {
            // ler os dados do arquivo
            rules = Files.readString(RULES_FILE);
        } catch (IOException e) {
            // Se ocorrer uma exceção durante a leitura do arquivo, registre o erro
            LogUtils.getLogger(RulesUtils.class).error(e.getMessage(), e);
        }
    }

    // Construtor privado para evitar a instanciação da classe
    private RulesUtils() {
    }

    /**
     * Recupera as regras do arquivo.
     *
     * @return Uma string contendo as regras lidas do arquivo.
     */
    public static String getRules() {
        return rules;
    }
}
