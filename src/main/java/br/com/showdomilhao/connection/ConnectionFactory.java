package br.com.showdomilhao.connection;

import br.com.showdomilhao.util.LogUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Obtém conexões para o banco de dados
 */
public final class ConnectionFactory {
    /**
     * Conexão
     */
    private static Connection connection;
    /**
     * URL para conectar-se ao banco em arquivo
     */
    private static final String URL_CONNECTION = "jdbc:sqlite:src/main/resources/database/show_milhao.db";

    /**
     * construtor privado para não permitir instanciação da classe
     */
    private ConnectionFactory() {
    }

    // Bloco estático é executado antes do construtor
    static {
        connect();
    }

    /**
     * Padrão Singleton
     */
    private static void connect() {
        if (connection == null) {
            try {
                // Abre uma nova conexão com o banco de dados
                connection = DriverManager.getConnection(URL_CONNECTION);
                // transação automática desativada
                connection.setAutoCommit(false);
            } catch (SQLException e) {
                LogUtils.getLogger(ConnectionFactory.class).error(e.getMessage(), e);
            }
        }
    }

    /**
     * @return Nova conexão
     */
    public static Connection getConnection() {
        return connection;
    }
}
