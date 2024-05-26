package br.com.showdomilhao.controller;

import br.com.showdomilhao.application.ContinuousReproduction;
import br.com.showdomilhao.model.Difficulty;
import br.com.showdomilhao.model.Player;
import br.com.showdomilhao.model.Question;
import br.com.showdomilhao.model.Ranking;
import br.com.showdomilhao.service.QuestionService;
import br.com.showdomilhao.service.RankingService;
import br.com.showdomilhao.util.FXUtils;
import br.com.showdomilhao.util.LogUtils;
import br.com.showdomilhao.util.SoundUtils;
import com.jfoenix.controls.JFXButton;
import javafx.animation.FadeTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.util.Duration;
import javafx.util.converter.NumberStringConverter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Controlador para a tela principal do Show do Milhão.
 * <p>
 * Esta classe gerencia a lógica do jogo, incluindo exibição de perguntas,
 * verificação de respostas, atualização de pontuações e navegação entre telas.
 */
public class ControllerLayoutMainScreen {

    @FXML
    private Button btnEnunciated;
    @FXML
    private Label lblUniversityStudent1;
    @FXML
    private Label lblUniversityStudent2;
    @FXML
    private Label lblUniversityStudent3;
    @FXML
    private Button btnUniversityStudent1;
    @FXML
    private Button btnUniversityStudent2;
    @FXML
    private Button btnUniversityStudent3;
    @FXML
    private Button btnAlternative1;
    @FXML
    private Button btnAlternative2;
    @FXML
    private Button btnAlternative3;
    @FXML
    private Button btnAlternative4;
    @FXML
    private Label lblPlayerName;
    @FXML
    private Label lblRoundNumber;
    @FXML
    private Label lblQuestionNumber;
    @FXML
    private Label lblLevel;
    @FXML
    private Label lblHitScore;
    @FXML
    private Label lblErrorScore;
    @FXML
    private Label lblStopScore;
    @FXML
    private Label lblQuantityOfJumps;
    @FXML
    private JFXButton btnLetters1;
    @FXML
    private JFXButton btnLetters2;
    @FXML
    private JFXButton btnLetters3;
    @FXML
    private JFXButton btnLetters4;
    @FXML
    private JFXButton btnMainLetters;
    @FXML
    private Label lblDescriptionLetters;
    @FXML
    private JFXButton btnJump;
    @FXML
    private Label lblDescriptionJump;
    @FXML
    private JFXButton btnMainUniversityStudent;
    @FXML
    private Label lblDescriptionUniversityStudent;

    private final Player player;

    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getCurrencyInstance();

    private final List<Integer> idsQuestionAsked = new ArrayList<>();

    private final QuestionService questionService;

    private final IntegerProperty countGeneratedQuestions = new SimpleIntegerProperty(1);

    private final IntegerProperty countGeneratedRounds = new SimpleIntegerProperty(1);

    private final IntegerProperty remainingJumps = new SimpleIntegerProperty(3);

    private final BooleanProperty visibleJump = new SimpleBooleanProperty(true);

    private final BooleanProperty visibleLetters = new SimpleBooleanProperty(true);

    private final BooleanProperty visibleUniversityStudent = new SimpleBooleanProperty(true);

    private final BooleanProperty lettersClicked = new SimpleBooleanProperty(false);

    private final BooleanProperty universityStudentClicked = new SimpleBooleanProperty(false);

    private final IntegerProperty hitScore = new SimpleIntegerProperty(1000);

    private final IntegerProperty errorScore = new SimpleIntegerProperty(0);

    private final IntegerProperty stopScore = new SimpleIntegerProperty(0);

    private final RankingService rankingService;

    private static final String LAYOUT_HOME = "/view/LayoutHomeScreen.fxml";

    private String responseHelp;

    private Button[] wrongAlternativesButtons = new Button[3];

    private static final String CONFIRM_MESSAGE = "Você está certo disso?";

    /**
     * Construtor da classe ControllerLayoutMainScreen.
     * <p>
     * Inicializa o jogador, o serviço de perguntas e o serviço de ranking.
     */
    public ControllerLayoutMainScreen() {
        this.player = FXUtils.getPlayer();
        questionService = new QuestionService();
        rankingService = new RankingService();
    }

    /**
     * Inicializa a tela principal e seus componentes.
     * <p>
     * Configura bindings, música de fundo e processa a primeira pergunta.
     */
    @FXML
    private void initialize() {
        // Audio
        FXUtils.changeVoice("/songs/1-mil-reais-voice.mp3");
        // Inicia música de fundo após 4 segundos com o volume em 20%
        backgroundSound(Duration.seconds(4), toggleBackgroundSound(), 0.2);

        lblPlayerName.textProperty().bindBidirectional(player.nameProperty());
        lblRoundNumber.textProperty().bindBidirectional(countGeneratedRounds, new NumberStringConverter());
        lblQuestionNumber.textProperty().bindBidirectional(countGeneratedQuestions, new NumberStringConverter());
        // A propriedade 'countRoundGenerated' é monitorada no segundo parâmetro. O primeiro parâmetro depende do valor atual de 'countRoundGenerated' para gerar o texto correto para o Label. Assim, sempre que 'countRoundGenerated' mudar, o texto do Label será atualizado automaticamente para refletir a nova dificuldade correspondente.
        lblLevel.textProperty().bind(Bindings.createStringBinding(() -> Objects.requireNonNull(Difficulty.fromValue(countGeneratedRounds.get())).getDescriptionPtBr(), countGeneratedRounds));
        lblHitScore.textProperty().bindBidirectional(hitScore, new NumberStringConverter(NUMBER_FORMAT));
        lblErrorScore.textProperty().bindBidirectional(errorScore, new NumberStringConverter(NUMBER_FORMAT));
        lblStopScore.textProperty().bindBidirectional(stopScore, new NumberStringConverter(NUMBER_FORMAT));

        processQuestion(Difficulty.EASY);

        // Desabilita o botão "pulo" quando a quantidade de pulos chega a zero.
        btnJump.disableProperty().bind(remainingJumps.isEqualTo(0).or(visibleJump.not()));
        lblDescriptionJump.disableProperty().bind(remainingJumps.isEqualTo(0).or(visibleJump.not()));
        lblQuantityOfJumps.textProperty().bindBidirectional(remainingJumps, new NumberStringConverter());

        lblUniversityStudent1.visibleProperty().bind(visibleUniversityStudent.not().and(universityStudentClicked.not()));
        lblUniversityStudent2.visibleProperty().bind(visibleUniversityStudent.not().and(universityStudentClicked.not()));
        lblUniversityStudent3.visibleProperty().bind(visibleUniversityStudent.not().and(universityStudentClicked.not()));
        btnUniversityStudent1.visibleProperty().bind(visibleUniversityStudent.not().and(universityStudentClicked.not()));
        btnUniversityStudent2.visibleProperty().bind(visibleUniversityStudent.not().and(universityStudentClicked.not()));
        btnUniversityStudent3.visibleProperty().bind(visibleUniversityStudent.not().and(universityStudentClicked.not()));
        btnMainUniversityStudent.disableProperty().bind(visibleUniversityStudent.not());
        lblDescriptionUniversityStudent.disableProperty().bind(visibleUniversityStudent.not());

        btnLetters1.visibleProperty().bind(visibleLetters.not().and(lettersClicked.not()));
        btnLetters2.visibleProperty().bind(visibleLetters.not().and(lettersClicked.not()));
        btnLetters3.visibleProperty().bind(visibleLetters.not().and(lettersClicked.not()));
        btnLetters4.visibleProperty().bind(visibleLetters.not().and(lettersClicked.not()));
        btnMainLetters.disableProperty().bind(visibleLetters.not());
        lblDescriptionLetters.disableProperty().bind(visibleLetters.not());
    }

    /**
     * Fecha o aplicativo, tocando um áudio de despedida antes de sair.
     */
    @FXML
    public void close() {
        FXUtils.changeVoice("/songs/tchau-pessoal-até-a-proxima-vez-voice.mp3");
        FXUtils.setDelayTransition(Duration.seconds(2), event -> FXUtils.exit());

    }

    /**
     * Valores iniciais
     * <p>
     * Método responsável por inicializar os valores das variáveis da tela principal.
     */
    private void initLabels() {
        countGeneratedRounds.set(1);
        countGeneratedQuestions.set(1);
        hitScore.set(1000);
        errorScore.set(0);
        stopScore.set(0);
        idsQuestionAsked.clear();
        remainingJumps.set(3);
        visibleLetters.set(true);
        visibleUniversityStudent.set(true);
    }

    /**
     * Método responsável por formatar a lista de ids das perguntas feitas para a sintaxe sql ex: (1,4,6)
     *
     * @return Ids na sintaxe do sql
     */
    private String getIdsQuestionAskedFormatted() {
        return idsQuestionAsked.isEmpty() ? "" : idsQuestionAsked.stream().map(String::valueOf).collect(Collectors.joining(",", "(", ")"));
    }

    /**
     * Processa a próxima pergunta
     *
     * @param difficulty Nível de pergunta
     */
    private void processQuestion(Difficulty difficulty) {
        try {

            // Verifica se o botão universitários está desabilitado
            if (btnMainUniversityStudent.disableProperty().get()) {
                universityStudentClicked.set(true);
            }

            // Verifica se o botão cartas está desabilitado
            if (btnMainLetters.disableProperty().get()) {
                enableAlternativeVisibility();
            }

            // busca a próxima pergunta pelo nivel
            Question question = questionService.findByLevel(getIdsQuestionAskedFormatted(), difficulty);

            if (Objects.nonNull(question)) {
                // adiciona o id da pergunta encontrada à lista de perguntas feitas
                idsQuestionAsked.add(question.getId());
                // insire a pergunta na tela principal
                defineQuestionOnScreen(question);
                // Verifica a resposta
                checkAnswer(question.getResponse());
            }

        } catch (NoSuchElementException e) {
            FXUtils.showErrorDialog("Pergunta para nível '%s' não encontrada no banco de dados.".formatted(difficulty.getDescriptionPtBr()));
        } catch (Exception e) {
            LogUtils.getLogger(this).error(e.getMessage(), e);
        }
    }

    /**
     * Define a pergunta na tela principal.
     * <p>
     * Este método define o enunciado da pergunta e embaralha as alternativas,
     * atribuindo-as aos botões correspondentes.
     *
     * @param question pergunta a ser exibida
     */
    private void defineQuestionOnScreen(Question question) {
        // adiciona o enunciado na tela
        btnEnunciated.setText(question.getEnunciated());

        // embaralha as ordens dos botões
        ObservableList<Button> randomButtons = FXCollections.observableArrayList(btnAlternative1, btnAlternative2, btnAlternative3, btnAlternative4);
        FXCollections.shuffle(randomButtons);

        // adiciona todas as alternativas aos botões embaralhados.
        randomButtons.get(0).setText(question.getAlternative1());
        randomButtons.get(1).setText(question.getAlternative2());
        randomButtons.get(2).setText(question.getAlternative3());
        randomButtons.get(3).setText(question.getResponse());
    }

    /**
     * Verifica a resposta nas alternativas dos botões.
     * <p>
     * Este método verifica se a resposta fornecida corresponde a uma das alternativas exibidas
     * e aplica os eventos apropriados para alternativas corretas ou erradas.
     *
     * @param response resposta a ser verificada
     */
    private void checkAnswer(String response) {
        if (btnAlternative1.getText().equals(response)) {
            applyWrongAlternativeEvent(btnAlternative1, btnAlternative2, btnAlternative3, btnAlternative4);
            // insere o botão do botão correto
            setResponseHelp(btnAlternative1.getText());
            // clicou na alternativa correta
            btnAlternative1.setOnMouseClicked(event -> treatCorrectAlternative(btnAlternative1));

        } else if (btnAlternative2.getText().equals(response)) {
            applyWrongAlternativeEvent(btnAlternative2, btnAlternative1, btnAlternative3, btnAlternative4);
            // insere o botão do botão correto
            setResponseHelp(btnAlternative2.getText());
            // clicou na alternativa correta
            btnAlternative2.setOnMouseClicked(event -> treatCorrectAlternative(btnAlternative2));

        } else if (btnAlternative3.getText().equals(response)) {
            applyWrongAlternativeEvent(btnAlternative3, btnAlternative1, btnAlternative2, btnAlternative4);
            // insere o botão do botão correto
            setResponseHelp(btnAlternative3.getText());
            // clicou na alternativa correta
            btnAlternative3.setOnMouseClicked(event -> treatCorrectAlternative(btnAlternative3));

        } else {
            applyWrongAlternativeEvent(btnAlternative4, btnAlternative1, btnAlternative2, btnAlternative3);
            // insere o botão do botão correto
            setResponseHelp(btnAlternative4.getText());
            // clicou na alternativa correta
            btnAlternative4.setOnMouseClicked(event -> treatCorrectAlternative(btnAlternative4));
        }
    }

    /**
     * Define o botão que contém a alternativa correta.
     * <p>
     * Este método armazena a referência ao botão que possui a resposta correta.
     *
     * @param responseHelp texto do botão correto
     */
    private void setResponseHelp(String responseHelp) {
        this.responseHelp = responseHelp;
    }

    /**
     * Aplica eventos aos botões de alternativas erradas.
     * <p>
     * Este método define eventos de clique para os botões de alternativas erradas
     * e armazena esses botões para referência futura.
     *
     * @param correctButton botão correto
     * @param wrongButtons  botões de alternativas erradas
     */
    private void applyWrongAlternativeEvent(Button correctButton, Button... wrongButtons) {
        wrongAlternativesButtons = wrongButtons;
        Arrays.stream(wrongButtons).forEach(btn -> btn.setOnMouseClicked(event -> treatWrongAlternative(correctButton)));
    }

    /**
     * Trata a seleção da alternativa correta.
     * <p>
     * Este método aplica os efeitos visuais e sonoros para a seleção correta e
     * executa a lógica de confirmação da resposta.
     *
     * @param correctButton botão da alternativa correta
     */
    private void treatCorrectAlternative(Button correctButton) {
        // audio
        FXUtils.changeVoice(toggleAudioConfirmation());

        // verifica qual botão foi pressionado
        ButtonType confirmDialog = FXUtils.showConfirmDialog(CONFIRM_MESSAGE);

        if (confirmDialog.getText().equalsIgnoreCase("sim")) {

            // audio
            FXUtils.changeVoice("/songs/qual-e-a-resposta-certa-voice.mp3");

            // Cria um atraso de 2 segundos, a ação é executada após o atraso -> chama o método da Animção do botão piscando
            FXUtils.setDelayTransition(Duration.seconds(2), event -> makeTransitionCorrectAlternative(correctButton));
        }

    }

    /**
     * Verifica se o nível atual é Fácil.
     * <p>
     * Este método retorna verdadeiro se o número de perguntas geradas estiver
     * no intervalo correspondente ao nível Fácil.
     *
     * @return verdadeiro se estiver no nível Fácil, caso contrário, falso
     */
    private boolean isEasyLevel() {
        return countGeneratedQuestions.greaterThanOrEqualTo(1).and(countGeneratedQuestions.lessThanOrEqualTo(5)).get();
    }

    /**
     * Verifica se o nível atual é Normal.
     * <p>
     * Este método retorna verdadeiro se o número de perguntas geradas estiver
     * no intervalo correspondente ao nível Normal.
     *
     * @return verdadeiro se estiver no nível Normal, caso contrário, falso
     */
    private boolean isNormalLevel() {
        return countGeneratedQuestions.greaterThanOrEqualTo(6).and(countGeneratedQuestions.lessThanOrEqualTo(10)).get();
    }

    /**
     * Verifica se o nível atual é Difícil.
     * <p>
     * Este método retorna verdadeiro se o número de perguntas geradas estiver
     * no intervalo correspondente ao nível Difícil.
     *
     * @return verdadeiro se estiver no nível Difícil, caso contrário, falso
     */
    private boolean isDifficultLevel() {
        return countGeneratedQuestions.greaterThanOrEqualTo(11).and(countGeneratedQuestions.lessThanOrEqualTo(17)).get();
    }


    /**
     * Trata a seleção de uma alternativa errada.
     * <p>
     * Este método aplica os efeitos visuais e sonoros para a seleção incorreta e
     * executa a lógica de confirmação da resposta errada.
     *
     * @param correctAlternativeBtn botão da alternativa correta
     */
    private void treatWrongAlternative(Button correctAlternativeBtn) {
        // audio
        FXUtils.changeVoice(toggleAudioConfirmation());

        // verifica qual botão foi pressionado
        ButtonType confirmDialog = FXUtils.showConfirmDialog(CONFIRM_MESSAGE);

        if (confirmDialog.getText().equalsIgnoreCase("sim")) {
            // chama método para atualizar a pontuação do jogador
            updatePlayerScore(errorScore.get());
            // audio
            FXUtils.changeVoice("/songs/qual-e-a-resposta-certa-voice.mp3");

            // Cria um atraso de 2.5 segundos, a ação é executada após o atraso -> chama o método da Animção do botão piscando
            FXUtils.setDelayTransition(Duration.seconds(2.5), event -> makeTransitionWrongAlternative(correctAlternativeBtn));

        }
    }

    /**
     * Atualiza o ranking do jogador.
     * <p>
     * Este método salva ou atualiza a pontuação do jogador no ranking.
     *
     * @param score pontuação a ser atualizada
     */
    private void updatePlayerScore(int score) {
        Ranking rankingPlayer = rankingService.findById(player.getId());

        if (Objects.isNull(rankingPlayer)) {
            Ranking ranking = new Ranking();
            ranking.setPlayer(player);
            ranking.setPunctuation(score);

            //salvar no ranking o jogador
            rankingService.save(ranking);

        } else {
            if (rankingPlayer.getPunctuation() < score) {
                // atualiza a pontuação anterior
                rankingPlayer.setPunctuation(score);

                // salva a nova pontuação
                rankingService.update(rankingPlayer);
            }

        }
    }

    /**
     * Alterna o áudio de confirmação.
     * <p>
     * Este método retorna um caminho de áudio aleatório para a confirmação de ações.
     *
     * @return caminho do áudio de confirmação
     */
    private String toggleAudioConfirmation() {
        // Criando a caminho de audios
        List<String> audioConfirm = Arrays.asList("/songs/esta-certo-disso-voice.mp3", "/songs/voce-tem-certeza-voice.mp3", "/songs/posso-perguntar-void.mp3");

        // Embaralhando a lista
        Collections.shuffle(audioConfirm);

        // pega o primeiro elemento da lista embaralhado
        return audioConfirm.getFirst();
    }


    /**
     * Retorna uma transição de fade com configurações padrão.
     *
     * @return A transição de fade configurada com duração de 250 milissegundos, começando em 0 e terminando em 1,
     * e repetindo 8 vezes.
     */
    private FadeTransition getTransitionDefault() {
        // tempo de intervalo da piscada
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(250));
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.setCycleCount(8);// quantidade de piscadas
        return fadeTransition;
    }

    /**
     * Aplica uma animação de fade quando a resposta selecionada está errada.
     *
     * @param correctAlternativeBtn O botão que representa a alternativa correta.
     */
    private void makeTransitionWrongAlternative(Button correctAlternativeBtn) {
        // obtém a referência da animação
        FadeTransition transition = getTransitionDefault();
        // define qual elemento receberá a animação
        transition.setNode(correctAlternativeBtn);

        // audio junto com animação do botão
        FXUtils.changeVoice(toggleAudioError());

        // ação que será executada após a animação
        transition.setOnFinished(event -> {
            // Inicia música de fundo após 2.5 segundos com o volume em 100%
            backgroundSound(Duration.seconds(2.5), "/songs/som-abertura.mp3", 1);
            // audio
            FXUtils.changeVoice("/songs/que-pena-voce-errou-voice.mp3");

            // Cria um atraso de 3 segundos, a ação é executada após o atraso
            FXUtils.setDelayTransition(Duration.seconds(3), evt -> {
                FXUtils.changeLayout(getClass(), LAYOUT_HOME);
                initLabels(); // redefine os valores padrão
            });
        });

        // executa a animação
        transition.play();
    }

    /**
     * Retorna o caminho de um áudio de erro selecionado aleatoriamente.
     *
     * @return O caminho do áudio escolhido aleatoriamente de uma lista pré-definida de áudios de erro.
     */
    private String toggleAudioError() {
        // Criando a caminho de audios
        List<String> audioError = Arrays.asList("/songs/deu errado.mp3", "/songs/bem feito.mp3");

        // Embaralhando a lista
        Collections.shuffle(audioError);

        // pega o primeiro elemento da lista embaralhado
        return audioError.getFirst();
    }

    /**
     * Aplica uma animação quando a resposta selecionada está correta.
     *
     * @param correctAlternativeBtn O botão que representa a alternativa correta.
     */
    private void makeTransitionCorrectAlternative(Button correctAlternativeBtn) {
        // obtém a referência da animação
        FadeTransition transition = getTransitionDefault();
        // define qual elemento receberá a animação
        transition.setNode(correctAlternativeBtn);

        // audio
        FXUtils.changeVoice("/songs/som-de-acerto.mp3");

        // ação que será executada após a animação
        transition.setOnFinished(event -> {
            // audio
            FXUtils.changeVoice("/songs/certa-resposta.mp3");

            // Cria um atraso de 2 segundos, a ação é executada após o atraso
            FXUtils.setDelayTransition(Duration.seconds(2), evt -> processNextQuestionAndScore());

        });

        // executa a animação
        transition.play();
    }

    /**
     * Incrementa o contador de perguntas geradas.
     */
    private void incrementCounterQuestions() {
        countGeneratedQuestions.set(countGeneratedQuestions.get() + 1);
    }

    /**
     * Incrementa o contador de rodadas geradas.
     */
    private void incrementCountRound() {
        countGeneratedRounds.set(countGeneratedRounds.get() + 1);
    }

    /**
     * Atualiza as pontuações nas labels.
     *
     * @param hitScore   Pontuação de acerto.
     * @param errorScore Pontuação de erro.
     * @param stopScore  Pontuação de parar.
     */
    private void updateScore(int hitScore, int errorScore, int stopScore) {
        this.hitScore.set(hitScore);
        this.errorScore.set(errorScore);
        this.stopScore.set(stopScore);
    }

    /**
     * Atualiza a pontuação do jogador na tela e chama a próxima pergunta.
     */
    private void processNextQuestionAndScore() {
        // incrementa uma pergunta
        incrementCounterQuestions();

        // Primeira rodada de perguntas => nivel Fácil
        if (isEasyLevel()) {

            // Crie um atraso de 5 segundos. Logo após, chame a próxima pergunta.
            FXUtils.setDelayTransition(Duration.seconds(5), event -> processQuestion(Difficulty.EASY));

            switch (countGeneratedQuestions.get()) {
                case 2 -> {
                    // Inicia música de fundo após 4 segundos com o volume em 20%
                    backgroundSound(Duration.seconds(4), toggleBackgroundSound(), 0.2);
                    FXUtils.changeVoice("/songs/2-mil-reais-voice.mp3");
                    updateScore(2_000, 1_000, 500);
                }
                case 3 -> {
                    // Inicia música de fundo após 4 segundos com o volume em 20%
                    backgroundSound(Duration.seconds(4), toggleBackgroundSound(), 0.2);
                    FXUtils.changeVoice("/songs/3-mil-reais-voice.mp3");
                    updateScore(3_000, 2_000, 1_000);
                }
                case 4 -> {
                    // Inicia música de fundo após 5 segundos com o volume em 20%
                    backgroundSound(Duration.seconds(5), toggleBackgroundSound(), 0.2);
                    FXUtils.changeVoice("/songs/4-mil-reais-voice.mp3");
                    updateScore(4_000, 3_000, 1_500);
                }
                default -> {
                    // Inicia música de fundo após 4 segundos com o volume em 20%
                    backgroundSound(Duration.seconds(4), toggleBackgroundSound(), 0.2);
                    FXUtils.changeVoice("/songs/5-mil-reais-voice.mp3");
                    updateScore(5_000, 4_000, 2_000);
                }
            }


            // Segunda rodada de perguntas => nivel Normal
        } else if (isNormalLevel()) {

            // Crie um atraso de 6 segundos. Logo após, chame a próxima pergunta.
            FXUtils.setDelayTransition(Duration.seconds(6), event -> processQuestion(Difficulty.NORMAL));

            switch (countGeneratedQuestions.get()) {
                case 6 -> {
                    // incrementa uma nova rodada de perguntas
                    incrementCountRound();
                    // Inicia música de fundo após 6 segundos com o volume em 20%
                    backgroundSound(Duration.seconds(6), toggleBackgroundSound(), 0.2);
                    FXUtils.changeVoice("/songs/10-mil-reais-voice.mp3");
                    updateScore(10_000, 5_000, 2_500);
                }
                case 7 -> {
                    // Inicia música de fundo após 4 segundos com o volume em 20%
                    backgroundSound(Duration.seconds(4), toggleBackgroundSound(), 0.2);
                    FXUtils.changeVoice("/songs/20-mil-reais-voice.mp3");
                    updateScore(20_000, 10_000, 5_000);
                }
                case 8 -> {
                    // Inicia música de fundo após 4 segundos com o volume em 20%
                    backgroundSound(Duration.seconds(4), toggleBackgroundSound(), 0.2);
                    FXUtils.changeVoice("/songs/30-mil-reais-voice.mp3");
                    updateScore(30_000, 20_000, 10_000);
                }
                case 9 -> {
                    // Inicia música de fundo após 4 segundos com o volume em 20%
                    backgroundSound(Duration.seconds(4), toggleBackgroundSound(), 0.2);
                    FXUtils.changeVoice("/songs/40-mil-reais-voice.mp3");
                    updateScore(40_000, 30_000, 15_000);
                }
                default -> {
                    // Inicia música de fundo após 4 segundos com o volume em 20%
                    backgroundSound(Duration.seconds(4), toggleBackgroundSound(), 0.2);
                    FXUtils.changeVoice("/songs/50-mil-reais-voice.mp3");
                    updateScore(50_000, 40_000, 20_000);
                }
            }

            // Terceira rodada de perguntas => nivel Difícil
        } else if (isDifficultLevel()) {

            // Crie um atraso de 6 segundos. Logo após, chame a próxima pergunta.
            FXUtils.setDelayTransition(Duration.seconds(6), event -> processQuestion(Difficulty.DIFFICULT));

            switch (countGeneratedQuestions.get()) {
                case 11 -> {
                    // incrementa uma nova rodada de perguntas
                    incrementCountRound();
                    // Inicia música de fundo após 6 segundos com o volume em 20%
                    backgroundSound(Duration.seconds(6), toggleBackgroundSound(), 0.2);
                    FXUtils.changeVoice("/songs/100-mil-reais-voice2.mp3");
                    updateScore(100_000, 50_000, 25_000);
                }
                case 12 -> {
                    // Inicia música de fundo após 6 segundos com o volume em 20%
                    backgroundSound(Duration.seconds(6), toggleBackgroundSound(), 0.2);
                    FXUtils.changeVoice("/songs/200-mil-reais-voice.mp3");
                    updateScore(200_000, 100_000, 50_000);
                }
                case 13 -> {
                    // Inicia música de fundo após 4 segundos com o volume em 20%
                    backgroundSound(Duration.seconds(4), toggleBackgroundSound(), 0.2);
                    FXUtils.changeVoice("/songs/300-mil-reais-voice.mp3");
                    updateScore(300_000, 200_000, 100_000);
                }
                case 14 -> {
                    // Inicia música de fundo após 5 segundos com o volume em 20%
                    backgroundSound(Duration.seconds(5), toggleBackgroundSound(), 0.2);
                    FXUtils.changeVoice("/songs/400-mil-reais-voice.mp3");
                    updateScore(400_000, 300_000, 150_000);
                }
                case 15 -> {
                    // Inicia música de fundo após 5 segundos com o volume em 20%
                    backgroundSound(Duration.seconds(5), toggleBackgroundSound(), 0.2);
                    FXUtils.changeVoice("/songs/500-mil-reais-voice.mp3");
                    updateScore(500_000, 400_000, 200_000);
                }
                case 16 -> {
                    // Inicia música de fundo após 6 segundos com o volume em 20%
                    backgroundSound(Duration.seconds(6), toggleBackgroundSound(), 0.2);
                    FXUtils.changeVoice("/songs/1-milhao-reais-voice.mp3");
                    helpNotVisible();
                    updateScore(1_000_000, 500_000, 0);
                }
                default -> {
                    // Inicia música de fundo após 12 segundos com o volume em 100%
                    backgroundSound(Duration.seconds(10), "/songs/ritmo-de-festa-sound.mp3", 1);
                    updatePlayerScore(hitScore.get());
                    // Altera o layout
                    FXUtils.changeLayout(getClass(), "/view/LayoutCongratulationScreen.fxml");
                    initLabels(); // redefine os valores padrão
                }
            }
        }

    }

    /**
     * Observa quando o botão Parar é pressionado.
     */
    @FXML
    public void onStop() {
        // Audio
        FXUtils.changeVoice("/songs/vai-parar-esta-certo-disso-voice.mp3");

        // Verifica qual botão foi pressionado
        ButtonType confirmDialog = FXUtils.showConfirmDialog(CONFIRM_MESSAGE);

        if (confirmDialog.getText().equalsIgnoreCase("sim")) {

            if (countGeneratedQuestions.greaterThanOrEqualTo(2).and(countGeneratedQuestions.lessThanOrEqualTo(16)).get()) {
                // Audio
                FXUtils.changeVoice("/songs/posso-entregar-o-ouro-voice2.mp3");

                // Verifica qual botão foi pressionado
                ButtonType confirmDialogConfirm = FXUtils.showConfirmDialog(CONFIRM_MESSAGE);

                if (confirmDialogConfirm.getText().equalsIgnoreCase("sim")) {
                    handleStop();
                }
            } else {
                handleStop();
            }
        }
    }

    /**
     * Atualiza a pontuação quando o jogo é parado
     */
    private void handleStop() {
        if (countGeneratedQuestions.get() == 16) {
            FXUtils.changeVoice("/songs/parou-500-mil-reais-voice.mp3");
        } else {
            FXUtils.changeVoice("/songs/ok-parou-voice.mp3");
        }

        // visa que parou
        FXUtils.showStopDialog();
        //Troca para o som de abertura
        backgroundSound(Duration.seconds(0), "/songs/som-abertura.mp3", 1);
        // chama método para atualizar a pontuação do jogador
        updatePlayerScore(stopScore.get());
        // Altera o layout
        FXUtils.changeLayout(getClass(), "/view/LayoutRankingScreen.fxml");
        initLabels(); // redefine os valores padrão
    }

    /**
     * Observa quando o botão pular é pressionado
     */
    @FXML
    public void onJump() {
        // Audio
        FXUtils.changeVoice("/songs/você-pula-voice.mp3");

        // Verifica qual botão foi pressionado
        ButtonType confirmDialog = FXUtils.showConfirmDialog(CONFIRM_MESSAGE);

        if (confirmDialog.getText().equalsIgnoreCase("sim")) {

            decreaseJump();

            if (remainingJumps.isEqualTo(0).get()) {
                // Audio
                FXUtils.changeVoice("/songs/não-pode-mais-pular-voice.mp3");

                FXUtils.setDelayTransition(Duration.seconds(3.5), event -> handleJump());
            } else {
                handleJump();
            }
        }

        // define o foco para ficar no enunciado
        btnEnunciated.requestFocus();
    }

    /**
     * processa novas perguntas ao pular
     */
    private void handleJump() {
        // Audio
        FXUtils.changeVoice("/songs/pulou-proxima-pergunta-void.mp3");

        FXUtils.setDelayTransition(Duration.seconds(3.5), event -> {
            if (isEasyLevel()) {
                processQuestion(Difficulty.EASY);
            } else if (isNormalLevel()) {
                processQuestion(Difficulty.NORMAL);
            } else {
                processQuestion(Difficulty.DIFFICULT);
            }
        });


    }


    /**
     * Decrementar um pulo do valor atual.
     */
    private void decreaseJump() {
        remainingJumps.set(remainingJumps.get() - 1);
    }

    /**
     * Observa quando o botão Universitário é pressionado
     */
    @FXML
    public void onUniversityStudent() {
        visibleUniversityStudent.set(false);
        setNameUniversityStudent();
        // audio
        FXUtils.changeVoice("/songs/estao-presente-universitarios-voice.mp3");

        FXUtils.setDelayTransition(Duration.seconds(6), e -> {
            // audio
            FXUtils.changeVoice(toggleAudioUniversityStudent());
            FXUtils.setDelayTransition(Duration.seconds(6), event -> showOpinionOfUniversityStudents());
        });

        // define o foco para ficar no enunciado
        btnEnunciated.requestFocus();
    }

    /**
     * Método responsável por indicar pelo menos uma das alternativas corretas
     */
    private void showOpinionOfUniversityStudents() {
        List<String> alternatives = Arrays.asList(btnAlternative1.getText(), btnAlternative2.getText(), btnAlternative3.getText(), btnAlternative4.getText());
        // Embaralhando a lista com as alternativas
        Collections.shuffle(alternatives);

        ObservableList<Button> universityStudents = FXCollections.observableArrayList(btnUniversityStudent1, btnUniversityStudent2, btnUniversityStudent3);
        // Embaralhando a lista de universitários
        FXCollections.shuffle(universityStudents);

        // sempre haverá uma resposta correta
        // adiciona alguma das alternativas aos botões embaralhados.
        universityStudents.get(0).setText(responseHelp);
        universityStudents.get(1).setText(alternatives.get(1));
        universityStudents.get(2).setText(alternatives.get(2));
    }

    /**
     * Método responsável por definir os nomes dos universitários
     */
    private void setNameUniversityStudent() {
        lblUniversityStudent1.setText("Rogério Resende");
        lblUniversityStudent2.setText("Tadeu Espindola");
        lblUniversityStudent3.setText("Bruna Novais");
    }

    /**
     * Método para alternar o áudio de Universitários
     *
     * @return caminho do audido escolhido
     */
    private String toggleAudioUniversityStudent() {
        // Criando a caminho de audios
        List<String> audioError = Arrays.asList("/songs/vamos-ver-qual-e-a-opinião-dos-nossos-convidados-voice.mp3", "/songs/vamos-ver-qual-e-a-opinião-dos-seus-colegas-voice.mp3");

        // Embaralhando a lista
        Collections.shuffle(audioError);

        // pega o primeiro elemento da lista embaralhado
        return audioError.getFirst();
    }

    /**
     * Observa quando o botão Cartas é pressionado
     */
    @FXML
    public void onLetters() {
        visibleLetters.set(false);
        // audio
        FXUtils.changeVoice("/songs/tire-a-carta-do-baralho-voice.mp3");

        // Lista com os números que podem eliminar a quantidade de alternativas erradas
        List<Integer> amountEliminationAlternatives = Arrays.asList(0, 1, 2, 3);
        // Embaralhando a lista
        Collections.shuffle(amountEliminationAlternatives);

        // Insire os números embaralhados em cada uma das cartas
        btnLetters1.setText(String.valueOf(amountEliminationAlternatives.get(0)));
        btnLetters2.setText(String.valueOf(amountEliminationAlternatives.get(1)));
        btnLetters3.setText(String.valueOf(amountEliminationAlternatives.get(2)));
        btnLetters4.setText(String.valueOf(amountEliminationAlternatives.get(3)));

        // Observa qual carta das cartas será clicada
        btnLetters1.setOnMouseClicked(event -> checkClickedLetter(btnLetters1));
        btnLetters2.setOnMouseClicked(event -> checkClickedLetter(btnLetters2));
        btnLetters3.setOnMouseClicked(event -> checkClickedLetter(btnLetters3));
        btnLetters4.setOnMouseClicked(event -> checkClickedLetter(btnLetters4));

        // define o foco para ficar no enunciado
        btnEnunciated.requestFocus();
    }

    /**
     * Método responsável por obter a referência do botão clicado
     *
     * @param buttonClicked Botão da carta clicado
     */
    private void checkClickedLetter(Button buttonClicked) {
        // define que o botão das cartas foi clicado
        lettersClicked.set(true);
        //audio
        FXUtils.changeVoice("/songs/o-que-vao-dizer-as-cartas-voice.mp3");
        // chama o método para eliminar as alternativas
        FXUtils.setDelayTransition(Duration.seconds(3), event -> eliminateWrongAlternatives(buttonClicked));

        // define o foco para ficar no enunciado
        btnEnunciated.requestFocus();
    }

    /**
     * Método responsável por eliminar as alternativas erradas de acordo com a quantidade obtida da informação vinda do botão
     *
     * @param buttonClicked Botão da carta clicado
     */
    private void eliminateWrongAlternatives(Button buttonClicked) {

        switch (Integer.parseInt(buttonClicked.getText())) {
            case 0 ->
                //audio
                    FXUtils.changeVoice("/songs/tirou-um-rei-ficou-na-mesma-nenhuma-carta-eliminada-voice.mp3");
            case 1 -> {
                // audio
                FXUtils.changeVoice("/songs/tirou-o-numero-um-eliminar-1-resposta-errada-voice.mp3");
                // remove aleatoriamente a visibilidade de um botão errado
                FXUtils.setDelayTransition(Duration.seconds(3.5), event -> wrongAlternativesButtons[ThreadLocalRandom.current().nextInt(3)].setVisible(false));
            }
            case 2 -> {
                // audio
                FXUtils.changeVoice("/songs/tirou-um-2-eliminar-2-respostas-erradas-voice.mp3");
                // remove a visibilidade de dois botões errados
                FXUtils.setDelayTransition(Duration.seconds(3), event -> {
                    wrongAlternativesButtons[0].setVisible(false);
                    wrongAlternativesButtons[1].setVisible(false);
                });
            }
            default -> {
                // audio
                FXUtils.changeVoice("/songs/tirou-3-vamos-eliminar-3-respostas-erradas-voice.mp3");
                // remove a visibilidade de três botões errados
                FXUtils.setDelayTransition(Duration.seconds(7), event -> {
                    wrongAlternativesButtons[0].setVisible(false);
                    wrongAlternativesButtons[1].setVisible(false);
                    wrongAlternativesButtons[2].setVisible(false);
                });

            }
        }
    }

    /**
     * Ativa a visibilidade das alternativas quando são usadas as cartas
     */
    private void enableAlternativeVisibility() {
        for (Button alternativesButton : wrongAlternativesButtons) {
            // Verifica os botões não visíveis
            if (!alternativesButton.isVisible()) {
                alternativesButton.setVisible(true);
            }
        }

    }

    /**
     * Método privado para ajudar a tornar elementos não visíveis.
     * Atualiza algumas variáveis booleanas para ocultar elementos específicos.
     */
    private void helpNotVisible() {
        // Define as variáveis lettersClicked e universityStudentClicked como true,
        // indicando que esses elementos foram clicados.
        lettersClicked.set(true);
        universityStudentClicked.set(true);

        // Define as variáveis visibleLetters, visibleJump e visibleUniversityStudent como false,
        // indicando que esses elementos não devem ser visíveis na interface.
        visibleLetters.set(false);
        visibleJump.set(false);
        visibleUniversityStudent.set(false);
    }

    private void backgroundSound(Duration startIn, String pathOfMusic, double volume) {
        ContinuousReproduction openingSound = SoundUtils.openingSound(pathOfMusic);
        // Para a musica de abertura
        openingSound.stopSound();
        // Inicia uma nova música
        FXUtils.setDelayTransition(startIn, event -> openingSound.playSound().volume(volume));
    }


    /**
     * Método para alternar o som de fundo
     *
     * @return caminho do audido escolhido
     */
    private String toggleBackgroundSound() {
        // Criando a caminho de audios
        List<String> sound = Arrays.asList("/songs/trilha-sonora-de-suspense-n1-show-do-milhao-sound.mp3", "/songs/trilha-sonora-de-suspense-n2-show-do-milhao-sound.mp3", "/songs/trilha-sonora-de-suspense-n3-show-do-milhao-sound.mp3", "/songs/trilha-sonora-de-suspense-n4-show-do-milhao-sound.mp3");

        // Embaralhando a lista
        Collections.shuffle(sound);

        // Pega o primeiro elemento da lista embaralhado
        return sound.getFirst();
    }

}
