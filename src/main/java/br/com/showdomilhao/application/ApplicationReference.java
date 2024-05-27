package br.com.showdomilhao.application;

import br.com.showdomilhao.util.FXUtils;
import br.com.showdomilhao.util.SoundUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

/**
 * Classe principal da aplicação que inicializa e configura a interface gráfica do "Show do Milhão".
 */
public class ApplicationReference extends Application {

    private static final String ICON = "/image/logo-show-milhao.jpg";

    /**
     * Método principal que inicia a aplicação.
     *
     * @param args argumentos de linha de comando
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Inicializa e configura a interface gráfica da aplicação.
     *
     * @param primaryStage o estágio principal da aplicação
     * @throws Exception se ocorrer algum erro durante a inicialização
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Show do Milhão");

        // Carrega o layout
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LayoutHomeScreen.fxml"));
        Pane root = loader.load();

        // Pega o stage principal
        FXUtils.setStageRoot(primaryStage);

        // Adicionando o css ao root
        root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/cssFile/buttonStyle.css")).toExternalForm());
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);

        // Define o ícone do aplicativo.
        Image iconeImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(ICON)));
        primaryStage.getIcons().add(iconeImage);

        // Define o áudio com a música especificada
        ContinuousReproduction openingSound = SoundUtils.openingSound("/songs/som-abertura.mp3");
        // Indica que um Stage foi associado para fechar a thread que é usada na classe ContinuousReproduction quando necessário.
        openingSound.onStageDefined(primaryStage);
        // inicia o audio
        openingSound.playSound();

        // Remove todos os controles de janela padrão
        primaryStage.initStyle(StageStyle.UNDECORATED);

        // mostra a janela
        primaryStage.show();

    }
}
