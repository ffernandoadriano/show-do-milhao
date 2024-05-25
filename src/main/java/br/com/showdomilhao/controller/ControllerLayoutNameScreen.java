package br.com.showdomilhao.controller;

import br.com.showdomilhao.model.Player;
import br.com.showdomilhao.service.PlayerService;
import br.com.showdomilhao.util.FXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.util.Duration;

/**
 * Controlador para a tela de entrada do nome do jogador no layout do Show do Milhão.
 * <p>
 * Esta classe é responsável por gerenciar a entrada do nome do jogador e iniciar o jogo.
 */
public class ControllerLayoutNameScreen {

    private static final String WILL_START_VOICE = "/songs/vai-comecar-o-show-do-milhao-voice.mp3";
    private static final String PARTICIPANT_VOICE = "/songs/vamos-conhecer-agora-o-nosso-participante-voice.mp3";
    private static final String ERROR_VOICE = "/songs/que-pena-voce-errou-voice.mp3";

    @FXML
    private TextField txtPlayerName;

    private final PlayerService playerService;
    private final Player player;

    /**
     * Construtor da classe.
     * <p>
     * Inicializa o serviço de jogador e cria uma nova instância de jogador.
     */
    public ControllerLayoutNameScreen() {
        playerService = new PlayerService();
        player = new Player();
    }

    /**
     * Método chamado pelo JavaFX quando o controller é instanciado.
     * <p>
     * Inicializa a tela de entrada do nome do jogador, configurando os bindings dos
     * componentes da interface gráfica com as propriedades do jogador.
     */
    @FXML
    private void initialize() {
        FXUtils.changeVoice(PARTICIPANT_VOICE);
        player.nameProperty().bindBidirectional(txtPlayerName.textProperty());
    }

    /**
     * Manipulador de evento para retornar à tela inicial.
     * <p>
     * Altera o layout para a tela inicial.
     */
    @FXML
    public void goBack() {
        // Altera o layout
        FXUtils.changeLayout(getClass(), "/view/LayoutHomeScreen.fxml");
    }

    /**
     * Manipulador de evento para iniciar o jogo.
     * <p>
     * Verifica se o nome do jogador foi informado, salva o jogador no banco de dados e inicia o jogo.
     */
    @FXML
    public void start() {

        if (player.getName().isEmpty()) {
            FXUtils.changeVoice(ERROR_VOICE);
            FXUtils.showValidationError("Informe o nome do jogador!");
            return;
        }

        // Insere e/ou atualiza a referência do objeto, caso o nome já exista no banco de dados.
        playerService.save(player);

        // toca o audio
        FXUtils.changeVoice(WILL_START_VOICE);

        // Cria um atraso de 2.5 segundos, a ação é executada após o atraso
        FXUtils.setDelayTransition(Duration.seconds(2.5), event -> FXUtils.changeLayout(getClass(), "/view/LayoutMainScreen.fxml"));


    }

    /**
     * Manipulador de evento para fechar a aplicação.
     * <p>
     * Toca um áudio de despedida e, após um atraso de 2 segundos, encerra a aplicação.
     */
    @FXML
    public void close() {
        FXUtils.changeVoice("/songs/tchau-pessoal-até-a-proxima-vez-voice.mp3");
        FXUtils.setDelayTransition(Duration.seconds(2), event -> FXUtils.exit());
    }

    /**
     * Obtém o jogador atual.
     *
     * @return O jogador.
     */
    public Player getPlayer() {
        return player;
    }

}
