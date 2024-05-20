package br.com.showdomilhao.util;

import br.com.showdomilhao.application.Audio;
import br.com.showdomilhao.controller.ControllerLayoutNameScreen;
import br.com.showdomilhao.controller.ControllerLayoutQuestionAdditionScreen;
import br.com.showdomilhao.model.Player;
import br.com.showdomilhao.model.Question;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import static javafx.scene.control.Alert.AlertType.INFORMATION;

/**
 * Métodos utilitários relacionados à interface gráfica da aplicação
 */
public final class FXUtils {

    private static Stage primaryStage;

    private static Player player;

    private static Question question;

    private static String lblQuestionTitle;


    /**
     * Construtor privado para evitar a instanciação da classe
     */
    private FXUtils() {
    }

    /**
     * Define o stage principal da aplicação.
     *
     * @param stage o stage principal
     */
    public static void setStageRoot(Stage stage) {
        // pega o stage do layout principal
        if (Objects.nonNull(stage)) {
            primaryStage = stage;
        }
    }

    /**
     * Altera o layout da aplicação, definindo uma pergunta e título da janela pergunta.
     *
     * @param getClazz        a classe chamadora
     * @param layoutFXML      o caminho do arquivo FXML
     * @param currentQuestion a pergunta atual
     * @param questionTitle   o título da janela pergunta
     */
    public static void changeLayout(Class<?> getClazz, String layoutFXML, Question currentQuestion, String questionTitle) {
        question = currentQuestion;
        lblQuestionTitle = questionTitle;
        changeLayout(getClazz, layoutFXML);

    }

    /**
     * Altera o layout da aplicação.
     *
     * @param getClazz   a classe chamadora
     * @param layoutFXML o caminho do arquivo FXML
     */
    public static void changeLayout(Class<?> getClazz, String layoutFXML) {
        try {
            // Carrega o layout
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClazz.getResource(layoutFXML)));
            Pane newLayout = loader.load();

            // Obtém o controller associado ao FXML
            Object controller = loader.getController();

            // Obtém a referência do jogador atual
            if (controller instanceof ControllerLayoutNameScreen nameScreen) {
                player = nameScreen.getPlayer();

            } else if (controller instanceof ControllerLayoutQuestionAdditionScreen additionScreen) {
                additionScreen.getLblTitle().setText(lblQuestionTitle);
            }

            // muda de cena
            Scene scene = new Scene(newLayout, 800, 600);

            // define uma nova cena no stage principal
            primaryStage.setScene(scene);
        } catch (IOException e) {
            LogUtils.getLogger(FXUtils.class).error(e.getMessage(), e);
        }
    }

    /**
     * Encerra a aplicação.
     */
    public static void exit() {
        System.exit(0);
    }

    /**
     * Retorna o jogador atual.
     *
     * @return o jogador atual
     */
    public static Player getPlayer() {
        return player;
    }

    /**
     * Retorna a pergunta atual.
     *
     * @return a pergunta atual
     */
    public static Question getQuestion() {
        return question;
    }

    /**
     * Altera a voz da aplicação.
     *
     * @param pathOfTheVoice o caminho do arquivo de voz
     */
    public static void changeVoice(String pathOfTheVoice) {
        Audio audio = new Audio();
        audio.tocar(pathOfTheVoice);
    }

    /**
     * Configura um atraso de transição com a duração especificada e o manipulador de eventos fornecido.
     *
     * @param duration     a duração do atraso da transição
     * @param eventHandler o manipulador de eventos a ser executado após o término do atraso
     */
    public static void setDelayTransition(Duration duration, EventHandler<ActionEvent> eventHandler) {
        // Cria um objeto de transição de pausa com a duração especificada
        PauseTransition delay = new PauseTransition(duration);

        // Define o manipulador de eventos para ser executado após o término do atraso
        delay.setOnFinished(eventHandler);

        // Inicia a transição de pausa, que acionará o evento quando concluída
        delay.play();
    }


    /**
     * Exibe uma caixa de diálogo de alerta.
     *
     * @param message Mensagem de alerta a ser mostrada.
     */
    public static void showErrorDialog(String message) {
        // Cria o dialog com os dados
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(primaryStage);
        alert.setTitle("Erro");
        alert.setHeaderText(message);
        Platform.runLater(alert::showAndWait);
    }

    /**
     * Exibe uma caixa de diálogo de confirmação.
     *
     * @param message a mensagem de confirmação a ser mostrada
     * @return o botão pressionado
     */
    public static ButtonType showConfirmDialog(String message) {
        // Cria o dialog com os dados
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(primaryStage);
        alert.setTitle("Atenção");
        alert.setHeaderText(null);
        alert.setContentText(message);

        ButtonType buttonYes = new ButtonType("Sim");
        ButtonType buttonNo = new ButtonType("Não");

        alert.getButtonTypes().setAll(buttonYes, buttonNo);

        Optional<ButtonType> result = alert.showAndWait();

        return result.orElse(ButtonType.CANCEL);
    }

    /**
     * Exibe uma caixa de diálogo de confirmação para serviços.
     *
     * @param message a mensagem de confirmação a ser mostrada
     */
    public static void showConfirmDialogService(String message) {
        Alert alert = new Alert(INFORMATION);
        alert.initOwner(primaryStage);
        alert.setTitle("Processo concluído!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Exibe uma caixa de diálogo de Informação
     */
    public static void showStopDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(primaryStage);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText("Você parou!");
        alert.showAndWait();
    }

    /**
     * Exibe uma caixa de diálogo de erro de validação.
     *
     * @param message a mensagem de erro de validação a ser mostrada
     */
    public static void showValidationError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(primaryStage);
        alert.setTitle("Erro de validação");
        alert.setHeaderText("Informação incorreta");
        alert.setContentText(message);
        alert.showAndWait();
    }

}
