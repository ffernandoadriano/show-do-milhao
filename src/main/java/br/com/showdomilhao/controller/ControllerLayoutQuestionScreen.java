package br.com.showdomilhao.controller;

import br.com.showdomilhao.model.Difficulty;
import br.com.showdomilhao.model.Question;
import br.com.showdomilhao.service.QuestionService;
import br.com.showdomilhao.util.FXUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.util.Duration;

import java.util.List;


/**
 * Controlador para a tela de perguntas do layout do Show do Milhão.
 * <p>
 * Esta classe é responsável por gerenciar a tela de perguntas, incluindo
 * a exibição, adição, edição e exclusão de perguntas.
 */
public class ControllerLayoutQuestionScreen {

    @FXML
    private TableView<Question> table;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnDelete;

    private final QuestionService questionService;

    /**
     * Pergunta ativa no momento (selecionada ou em edição)
     */
    private Question currentQuestion;

    /**
     * Construtor da classe.
     * <p>
     * Inicializa o serviço de perguntas.
     */
    public ControllerLayoutQuestionScreen() {
        this.questionService = new QuestionService();
    }

    /**
     * Método chamado pelo JavaFX quando o controller é instanciado.
     * <p>
     * Inicializa a tela de perguntas carregando os dados e configurando
     * os bindings dos botões.
     */
    @FXML
    private void initialize() {
        // Carrega os dados na tela
        loadData(Difficulty.EASY);

        // Define os bindings gerais do modelo com a interface gráfica
        btnEdit.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
        btnDelete.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());

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
     * Manipulador de evento para carregar perguntas fáceis.
     */
    @FXML
    public void onEasy() {
        loadData(Difficulty.EASY);
    }

    /**
     * Manipulador de evento para carregar perguntas normais.
     */
    @FXML
    public void onNormal() {
        loadData(Difficulty.NORMAL);
    }

    /**
     * Manipulador de evento para carregar perguntas difíceis.
     */
    @FXML
    public void onDifficult() {
        loadData(Difficulty.DIFFICULT);
    }

    /**
     * Manipulador de evento para adicionar uma nova pergunta.
     * <p>
     * Altera o layout para a tela de adição de pergunta.
     */
    @FXML
    public void onAdd() {
        // Altera o layout
        FXUtils.changeLayout(getClass(), "/view/LayoutQuestionAdditionScreen.fxml", new Question(), "Nova Pergunta");
    }

    /**
     * Manipulador de evento para editar a pergunta selecionada.
     * <p>
     * Define a pergunta corrente como a pergunta selecionada na tabela
     * e altera o layout para a tela de edição de pergunta.
     */
    @FXML
    public void onEdit() {
        // Define a pergunta corrente como a pergunta selecionada na tabela
        currentQuestion = table.getSelectionModel().getSelectedItem();

        // Altera o layout
        FXUtils.changeLayout(getClass(), "/view/LayoutQuestionAdditionScreen.fxml", currentQuestion, "Editar Pergunta");
    }

    /**
     * Manipulador de evento para excluir a pergunta selecionada.
     * <p>
     * Exibe um diálogo de confirmação e, se confirmado, remove a pergunta
     * selecionada do banco de dados e recarrega os dados.
     */
    @FXML
    public void onDelete() {
        // Define a pergunta corrente como a pergunta selecionada na tabela
        currentQuestion = table.getSelectionModel().getSelectedItem();

        // audio
        FXUtils.changeVoice("/songs/esta-certo-disso-voice.mp3");

        // verifica qual botão foi pressionado
        ButtonType confirmDialog = FXUtils.showConfirmDialog("Você tem certeza que deseja excluir a pergunta?");

        if (confirmDialog.getText().equalsIgnoreCase("sim")) {
            // remove a pergunta no banco de dados
            questionService.delete(currentQuestion.getId());

            // Carrega os dados com o nível da pergunta excluída
            loadData(currentQuestion.getDifficulty());
        }
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
     * Carrega os dados na tela.
     *
     * @param difficulty O nível de dificuldade das perguntas a serem carregadas.
     */
    private void loadData(Difficulty difficulty) {
        List<Question> questionByLevel = questionService.findByLevel(difficulty);
        ObservableList<Question> questions = FXCollections.observableArrayList(questionByLevel);
        table.setItems(questions);

    }
}
