package br.com.showdomilhao.model;

import br.com.showdomilhao.util.LogUtils;

/**
 * Enumeração que representa os níveis de dificuldade.
 * Cada nível de dificuldade possui um valor numérico e uma descrição em português.
 */
public enum Difficulty {
    /**
     * Nível fácil.
     */
    EASY(1),

    /**
     * Nível normal.
     */
    NORMAL(2),

    /**
     * Nível difícil.
     */
    DIFFICULT(3);

    private final int level; // O nível de dificuldade
    private final String descriptionPtBr; // Descrição do nível em português

    /**
     * Construtor da enumeração Difficulty.
     *
     * @param level O nível de dificuldade associado ao enum
     */
    Difficulty(int level) {
        this.level = level;
        this.descriptionPtBr = defaultDescriptionPTBR(level);
    }

    /**
     * Método privado para atribuir uma descrição padrão com base no nível em pt-BR.
     *
     * @param level O nível de dificuldade
     * @return A descrição em português do nível de dificuldade
     */
    private String defaultDescriptionPTBR(int level) {
        return switch (level) {
            case 1 -> "Fácil";
            case 2 -> "Normal";
            case 3 -> "Difícil";
            default -> {
                LogUtils.getLogger(this).error("Nível inválido '{}' ", level);
                yield null;
            }
        };
    }

    /**
     * Método estático para retornar o tipo de enum com base no valor.
     *
     * @param value O valor numérico do nível de dificuldade
     * @return O enum correspondente ao valor fornecido
     */
    public static Difficulty fromValue(int value) {
        for (Difficulty difficulty : Difficulty.values()) {
            if (difficulty.getLevel() == value) {
                return difficulty;
            }
        }
        // Se nenhum valor correspondente for encontrado, lançar uma exceção ou retornar null, dependendo do seu caso de uso.
        LogUtils.getLogger(Difficulty.class).error("Valor do Nível é inválido '{}' ", value);
        return null;
    }

    /**
     * Método estático para retornar o tipo de enum com base no nome em português.
     *
     * @param name O nome em português do nível de dificuldade
     * @return O enum correspondente ao nome fornecido
     */
    public static Difficulty fromNamePTBR(String name) {
        for (Difficulty difficulty : Difficulty.values()) {
            if (difficulty.getDescriptionPtBr().equalsIgnoreCase(name)) {
                return difficulty;
            }
        }
        // Se nenhum valor correspondente for encontrado, lançar uma exceção ou retornar null, dependendo do seu caso de uso.
        LogUtils.getLogger(Difficulty.class).error("Nome do Nível é inválido '{}' ", name);
        return null;
    }

    /**
     * Obtém o nível de dificuldade associado ao enum.
     *
     * @return O nível de dificuldade
     */
    public int getLevel() {
        return level;
    }

    /**
     * Obtém a descrição em português do nível de dificuldade.
     *
     * @return A descrição em português
     */
    public String getDescriptionPtBr() {
        return descriptionPtBr;
    }
}
