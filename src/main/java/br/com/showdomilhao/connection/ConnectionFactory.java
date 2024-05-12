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
     * URL para conectar-se fora do JAR criado.
     */
    private static final String URL_CONNECTION_JAR = "jdbc:sqlite:data/show_milhao.db"; // Caminho para o arquivo do banco de dados fora do JAR

    /**
     * URL para conectar-se ao ambiente de desenvolvimento
     *
     * @apiNote Na URL '::resource:' de conexão, permite apenas acesso de leitura ao arquivo do banco de dados dentro do JAR. Isso ocorre porque o JAR é tratado como um recurso somente leitura pelo sistema de arquivos. Logo, não é permitido fazer CRUD dentro do JAR.
     */
    private static final String URL_CONNECTION_DEVELOPMENT = "jdbc:sqlite::resource:" + ConnectionFactory.class.getResource("/database/show_milhao.db");

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
                connection = DriverManager.getConnection(URL_CONNECTION_DEVELOPMENT);
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
