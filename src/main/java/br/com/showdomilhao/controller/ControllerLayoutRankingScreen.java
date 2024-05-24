package br.com.showdomilhao.controller;

import br.com.showdomilhao.model.Ranking;
import br.com.showdomilhao.service.RankingService;
import br.com.showdomilhao.util.FXUtils;
import br.com.showdomilhao.util.SoundUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.util.Duration;


/**
 * Controlador para a tela de ranking do layout do Show do Milhão.
 * <p>
 * Esta classe é responsável por inicializar a tela de ranking, fornecer
 * funcionalidades para retornar à tela inicial, limpar o ranking e fechar a aplicação.
 */
public class ControllerLayoutRankingScreen {

    @FXML
    private TableView<Ranking> tableRanking;

    /**
     * Objeto Service para manipulação dos dados
     */
    private final RankingService rankingService;

    /**
     * Construtor da classe.
     * <p>
     * Inicializa o serviço de ranking.
     */
    public ControllerLayoutRankingScreen() {
        this.rankingService = new RankingService();
    }

    /**
     * Método chamado pelo JavaFX quando o controller é instanciado.
     * <p>
     * Inicializa a tela de ranking carregando os dados.
     */
    @FXML
    private void initialize() {
        // Carrega os dados
        loadData();
    }

    /**
     * Manipulador de evento para retornar à tela inicial.
     * <p>
     * Se o jogador ganhou um milhão, toca a música de abertura padrão e redefine o estado.
     * Altera o layout para a tela inicial.
     */
    @FXML
    public void onGoBack() {

        // Ganhou um milhão? Se sim, executar a música de abertura padrão
        if (SoundUtils.isMadeMillion()) {
            SoundUtils.openingSound("/songs/som-abertura.mp3").playSound();
            SoundUtils.setMadeMillion(false);
        }
        // Altera o layout
        FXUtils.changeLayout(getClass(), "/view/LayoutHomeScreen.fxml");
    }

    /**
     * Manipulador de evento para limpar o ranking.
     * <p>
     * Toca um áudio de confirmação, exibe um diálogo de confirmação e, se confirmado,
     * apaga todos os dados de ranking e recarrega os dados.
     */
    @FXML
    public void onClearRanking() {
        // audio
        FXUtils.changeVoice("/songs/esta-certo-disso-voice.mp3");

        // verifica qual botão foi pressionado
        ButtonType confirmDialog = FXUtils.showConfirmDialog("Você tem certeza que deseja limpar o ranking?");

        if (confirmDialog.getText().equalsIgnoreCase("sim")) {
            rankingService.deleteAllRanking();
            // Carrega os dados
            loadData();
        }
    }

    /**
     * Manipulador de evento para fechar a aplicação.
     * <p>
     * Toca um áudio de despedida e, após um atraso de 2 segundos, encerra a aplicação.
     */
    @FXML
    public void onClose() {
        FXUtils.changeVoice("/songs/tchau-pessoal-até-a-proxima-vez-voice.mp3");
        FXUtils.setDelayTransition(Duration.seconds(2), event -> FXUtils.exit());
    }

    /**
     * Carrega os dados na tabela de ranking.
     * <p>
     * Busca todos os rankings do serviço e os insere na tabela.
     */
    private void loadData() {
        // busca todos os ranking
        ObservableList<Ranking> rankingObservableList = FXCollections.observableArrayList(rankingService.findAllByRanking());
        // inseri na tabela
        tableRanking.setItems(rankingObservableList);
    }
}
