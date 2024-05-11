package br.com.showdomilhao.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Classe utilitária para recuperar regras de um arquivo.
 */
public final class RulesUtils {

    /**
     * O caminho para o arquivo que contém as regras.
     */
    private static final String RULES_PATH = "/rules/regras.txt";

    /**
     * Variável de string para armazenar as regras.
     */
    private static String rules;

    /*
      Bloco de inicialização estático para ler as regras do arquivo.
      As regras são lidas do arquivo localizado em {@code RULES_PATH} e armazenadas na variável {@code rules}.
      Se ocorrer uma exceção durante a leitura do arquivo, será registrado um erro.
     */
    static {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(RulesUtils.class.getResourceAsStream(RULES_PATH))))) {
            rules = br.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            // Se ocorrer uma exceção durante a leitura do arquivo, registre o erro
            LogUtils.getLogger(RulesUtils.class).error(e.getMessage(), e);
        }
    }

    /**
     * Construtor privado para evitar a instanciação da classe.
     */
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

