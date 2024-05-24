package br.com.showdomilhao.controller;

import br.com.showdomilhao.model.Difficulty;
import br.com.showdomilhao.model.Question;
import br.com.showdomilhao.service.QuestionService;
import br.com.showdomilhao.util.FXUtils;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.util.Duration;

/**
 * Controlador para a tela de adição de perguntas do layout do Show do Milhão.
 * <p>
 * Esta classe é responsável por gerenciar a adição e edição de perguntas,
 * incluindo o nível de dificuldade e as alternativas.
 */
public class ControllerLayoutQuestionAdditionScreen {
    @FXML
    private Label lblTitle;
    @FXML
    private ToggleGroup groupLevel;
    @FXML
    private TextArea txtEnunciated;
    @FXML
    private TextField txtAlternative1;
    @FXML
    private TextField txtAlternative2;
    @FXML
    private TextField txtAlternative3;
    @FXML
    private TextField txtResponse;

    private final ObjectProperty<RadioButton> radioButtonSelected = new SimpleObjectProperty<>();

    private final QuestionService questionService;

    private final Question question;

    private final ObjectProperty<Difficulty> difficulty = new SimpleObjectProperty<>(Difficulty.EASY);

    private static final String QUESTION_SCREEN = "/view/LayoutQuestionScreen.fxml";

    /**
     * Construtor da classe.
     * <p>
     * Inicializa o serviço de perguntas e obtém a pergunta atual do FXUtils.
     */
    public ControllerLayoutQuestionAdditionScreen() {
        questionService = new QuestionService();
        question = FXUtils.getQuestion();
    }

    /**
     * Método chamado pelo JavaFX quando o controller é instanciado.
     * <p>
     * Inicializa a tela de adição de perguntas, configurando os bindings dos
     * componentes da interface gráfica com as propriedades da pergunta.
     */
    @FXML
    private void initialize() {

        // Verifica se é uma pergunta existente
        if (question.getId() != 0) {
            // Atualizar o nível de dificuldade na janela de acordo com o nível da pergunta selecionada
            groupLevel.selectToggle(mapDifficultyToToggle(question.getDifficulty()));
        }

        // a conversão do Toggle para RadioButton é automática
        // O método Bindings.select() é inteligente o suficiente para entender o tipo de dados da propriedade que está sendo selecionada e converter automaticamente para o tipo esperado pela propriedade que está sendo vinculada.
        radioButtonSelected.bind(Bindings.select(groupLevel.selectedToggleProperty()));
        // Só funciona o bind unilateral, pois o toggle do Radiobutton pode vir nulo e recebe o nível selecionado pelo usuário
        difficulty.bind(Bindings.createObjectBinding(() -> Difficulty.fromNamePTBR(radioButtonSelected.get().getText()), radioButtonSelected));
        question.difficultyProperty().bindBidirectional(difficulty);
        txtEnunciated.textProperty().bindBidirectional(question.enunciatedProperty());
        txtAlternative1.textProperty().bindBidirectional(question.alternative1Property());
        txtAlternative2.textProperty().bindBidirectional(question.alternative2Property());
        txtAlternative3.textProperty().bindBidirectional(question.alternative3Property());
        txtResponse.textProperty().bindBidirectional(question.responseProperty());
    }

    /**
     * Manipulador de evento para retornar à tela de perguntas.
     * <p>
     * Altera o layout para a tela de perguntas.
     */
    @FXML
    public void goBack() {
        FXUtils.changeLayout(getClass(), QUESTION_SCREEN);
    }

    /**
     * Manipulador de evento para salvar a pergunta atual.
     * <p>
     * Se a pergunta for nova, ela é adicionada ao banco de dados; caso contrário,
     * a pergunta existente é atualizada.
     */
    @FXML
    public void onSave() {

        // Verifica se é uma pergunta nova
        if (question.getId() == 0) {
            // adiciona a nova pergunta no banco de dados
            questionService.save(question);
            FXUtils.changeLayout(getClass(), QUESTION_SCREEN);

        } else {
            // atualiza a pergunta já existente
            questionService.update(question);
            FXUtils.changeLayout(getClass(), QUESTION_SCREEN);
        }

    }

    /**
     * Manipulador de evento para fechar a aplicação.
     * <p>
     * Toca um áudio de despedida e, após um atraso de 2 segundos, encerra a aplicação.
     */
    @FXML
    public void close() {
        //audio
        FXUtils.changeVoice("/songs/tchau-pessoal-até-a-proxima-vez-voice.mp3");
        FXUtils.setDelayTransition(Duration.seconds(2), event -> FXUtils.exit());
    }

    /**
     * Obtém o título do label.
     *
     * @return O label do título.
     */
    public Label getLblTitle() {
        return lblTitle;
    }

    /**
     * Mapeia um nível de dificuldade para o respectivo Toggle.
     *
     * @param difficulty O nível de dificuldade.
     * @return O Toggle correspondente ao nível de dificuldade.
     */
    private Toggle mapDifficultyToToggle(Difficulty difficulty) {
        return switch (difficulty) {
            case EASY -> groupLevel.getToggles().get(0);
            case NORMAL -> groupLevel.getToggles().get(1);
            default -> groupLevel.getToggles().get(2);
        };
    }
}
