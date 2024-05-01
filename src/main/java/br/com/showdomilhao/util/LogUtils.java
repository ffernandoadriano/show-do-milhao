package br.com.showdomilhao.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe utilitária para obtenção de objetos Logger.
 */
public final class LogUtils {

    // Construtor privado para evitar a instanciação da classe
    private LogUtils() {
    }

    /**
     * Obtém um objeto Logger para a classe do objeto fornecido.
     *
     * @param object O objeto para o qual deseja-se obter um Logger.
     * @return Um objeto Logger para a classe do objeto fornecido.
     */
    public static Logger getLogger(Object object) {
        return LoggerFactory.getLogger(object.getClass());
    }
}
