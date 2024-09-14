package aulaoo;

import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class App extends Application {

    private static App instance;
    private ControladorQuiz controladorQuiz;
    private Stage primaryStage;

    private VBox root;
    private Text enunciado;
    private Button alternativa1;
    private Button alternativa2;
    private Button alternativa3;
    private Button alternativa4;
    private Button alternativa5;
    private Text resultado;
    private Button proxima;
    private Button reiniciar;
    

    @Override
    public void start(Stage stage) throws Exception {
        instance = this;
        primaryStage = stage;
        mostrarTelaInicial();
    }

    public static App getInstance() {
        return instance;
    }

   public void mostrarTelaInicial(String mensagem) {
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("tela-inicial"); // adiciona a classe para a tela inicial

        if (mensagem != null && !mensagem.isEmpty()) {
            Text mensagemTexto = new Text(mensagem);
            mensagemTexto.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #333;");
            root.getChildren().add(mensagemTexto);
        }
    

        Button cadastrarPerguntasBtn = new Button("Cadastrar Perguntas");
        cadastrarPerguntasBtn.getStyleClass().add("botao-inicial"); // aplica style
        cadastrarPerguntasBtn.setOnAction(e -> mostrarTelaCadastro());

        Button iniciarQuizBtn = new Button("Iniciar Quiz");
        iniciarQuizBtn.getStyleClass().add("botao-inicial"); // aplica style
        iniciarQuizBtn.setOnAction(e -> {
            initQuiz();
            try {
                mostrarTelaQuiz();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        root.getChildren().addAll(cadastrarPerguntasBtn, iniciarQuizBtn);

        Scene scene = new Scene(root, 400, 300);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Tela Inicial");
        primaryStage.show();
    }

    public void mostrarTelaInicial() {
        mostrarTelaInicial("");
    }

    private void mostrarTelaCadastro() {
        CadastroPerguntas cadastroPerguntas = new CadastroPerguntas(primaryStage, this);
        cadastroPerguntas.mostrar();
    }

    private void mostrarTelaQuiz() throws Exception {

        inicializaComponentes();
        initQuiz();  //inicia o quiz
        atualizaComponentes();  //atualiza com a pergunta

        primaryStage.setTitle("Quiz App");
        primaryStage.setScene(criarCenaQuiz());
        primaryStage.show();
    }

    private void initQuiz() {
        GerenciarPerguntas gerenciador = new GerenciarPerguntas("ArmazenarPerguntas.txt");
        ArrayList<Questao> lista;

        try {
            lista = gerenciador.carregarPerguntas();

        } catch (IOException e) {
            e.printStackTrace();
            lista = new ArrayList<>();
        }

        if (lista.isEmpty()) {
            mostrarTelaInicial("Nenhuma pergunta cadastrada!");
        } else {
            controladorQuiz = new ControladorQuiz(lista);
        }

        controladorQuiz = new ControladorQuiz(lista);
    }

    private Button criaBotaoAlternativa(String texto) {
        Button botao = new Button(texto);
        botao.setPrefWidth(200);
        botao.getStyleClass().add("botao");
        botao.setTooltip(new Tooltip("Clique para responder..."));
        botao.setOnAction(respondeQuestao());
        return botao;
    }

    private void inicializaComponentes() {

        enunciado = new Text("Enunciado");

        alternativa1 = criaBotaoAlternativa("Questão 1");
        alternativa2 = criaBotaoAlternativa("Questão 2");
        alternativa3 = criaBotaoAlternativa("Questão 3");
        alternativa4 = criaBotaoAlternativa("Questão 4");
        alternativa5 = criaBotaoAlternativa("Questão 5");
        
        resultado = new Text();
        resultado.getStyleClass().add("texto-resultado"); // aplicando a classe css

        proxima = new Button("Próxima");
        proxima.getStyleClass().add("botao-proxima"); // aplicando a classe css
        proxima.setOnAction(proximaQuestao());
        //reiniciar
        reiniciar = new Button("Reiniciar");
        reiniciar.getStyleClass().add("botao-reiniciar"); // aplicando a classe css
        reiniciar.setOnAction(reiniciarQuiz());
        reiniciar.setVisible(false);
        
    }

    private Scene criarCenaQuiz() {

        root = new VBox();
        root.getStyleClass().add("root"); 

        //adiciona o logo
        Image logo = new Image("https://www.pngmart.com/files/19/Quiz-PNG-Clipart.png"); //arquivo da imagem do logo
        ImageView logoView = new ImageView(logo);
        logoView.setFitHeight(90); //altura
        logoView.setPreserveRatio(true); //proporção
        VBox.setMargin(logoView, new Insets(10, 0, 10, 0)); //margem ao redor

        root.getChildren().add(logoView); // add a logo ao topo do layout

        root.getChildren().add(enunciado);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(5.0);

        root.getChildren().add(alternativa1);
        root.getChildren().add(alternativa2);
        root.getChildren().add(alternativa3);
        root.getChildren().add(alternativa4);
        root.getChildren().add(alternativa5);
        root.getChildren().addAll(resultado, proxima, reiniciar);
        
        resultado.setVisible(false);
        proxima.setVisible(false);

        Scene cena = new Scene(root, 600, 400);
        cena.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        return cena;

    }

    public void atualizaComponentes() {

        Questao objQuestao = controladorQuiz.getQuestao();
        ArrayList<String> questoes = objQuestao.getTodasAlternativas();

        enunciado.setText(objQuestao.getEnunciado());
        enunciado.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #333;"); //negrito


        alternativa1.setVisible(false);
    alternativa2.setVisible(false);
    alternativa3.setVisible(false);
    alternativa4.setVisible(false);
    alternativa5.setVisible(false);

    // Exibe somente os botões necessários
    if (questoes.size() > 0) {
        alternativa1.setText(questoes.get(0));
        alternativa1.setVisible(true);
    }
    if (questoes.size() > 1) {
        alternativa2.setText(questoes.get(1));
        alternativa2.setVisible(true);
    }
    if (questoes.size() > 2) {
        alternativa3.setText(questoes.get(2));
        alternativa3.setVisible(true);
    }
    if (questoes.size() > 3) {
        alternativa4.setText(questoes.get(3));
        alternativa4.setVisible(true);
    }
    if (questoes.size() > 4) {
        alternativa5.setText(questoes.get(4));
        alternativa5.setVisible(true);
    }

        resultado.setVisible(false);
        proxima.setVisible(false);

    }

    private EventHandler <ActionEvent> respondeQuestao() {
        return event -> {
            
                Button clicado = (Button) event.getSource();
                String alternativa = clicado.getText();

                try {
                    boolean result = controladorQuiz.respondeQuestao(alternativa);

                if (result) {
                    resultado.setText("Acertou!");
                    resultado.setStyle("-fx-fill: green; -fx-font-size: 18px; -fx-font-weight: bold;");
                } else {
                    resultado.setText("Errou!");
                    resultado.setStyle("-fx-fill: red; -fx-font-size: 18px; -fx-font-weight: bold;");
                }

                 // oculta as outras alternativas
                 alternativa1.setVisible(clicado == alternativa1);
                 alternativa2.setVisible(clicado == alternativa2);
                 alternativa3.setVisible(clicado == alternativa3);
                 alternativa4.setVisible(clicado == alternativa4);
                 alternativa5.setVisible(clicado == alternativa5);

                resultado.setVisible(true);
                proxima.setVisible(true);      

                } catch (IndexOutOfBoundsException e) {
        
                resultado.setVisible(true);
                proxima.setVisible(false);
         }
    };

}

    private EventHandler <ActionEvent> proximaQuestao() {
        return event -> { 

                if (controladorQuiz.temProximaQuestao()) {
                    controladorQuiz.proximaQuestao();
                    atualizaComponentes();
                }  else {

                    int acertos = controladorQuiz.getAcertos();
                    int erros = controladorQuiz.getErros();

                    resultado.setText("Quiz finalizado! Acertos: " + controladorQuiz.getAcertos() + " Erros: " + controladorQuiz.getErros());

                    if (acertos > erros) {
                        resultado.setStyle("-fx-fill: green; -fx-font-size: 18px; -fx-font-weight: bold;");
                    } else if (erros > acertos) {
                        resultado.setStyle("-fx-fill: red; -fx-font-size: 18px; -fx-font-weight: bold;");
                    } else {
                        resultado.setStyle("-fx-fill: black; -fx-font-size: 18px; -fx-font-weight: bold;");
                    }
            
                    
                    desabilitarBotoes();
                    resultado.setVisible(true);
                    proxima.setVisible(false);
                    reiniciar.setVisible(true);
                }
            
        };
    }

    private EventHandler<ActionEvent> reiniciarQuiz() {
        return event -> {
            controladorQuiz.reiniciar(); 
            atualizaComponentes(); 
            habilitarBotoes(); 
            resultado.setVisible(false); // ocultao resultado
            proxima.setVisible(true); // oculta o proxima
            reiniciar.setVisible(false); // oculta o botao

        };
    }

    private void habilitarBotoes() {
        alternativa1.setDisable(false);
        alternativa2.setDisable(false);
        alternativa3.setDisable(false);
        alternativa4.setDisable(false);
        alternativa5.setDisable(false);
    }

    private void desabilitarBotoes() {
        alternativa1.setDisable(true);
        alternativa2.setDisable(true);
        alternativa3.setDisable(true);
        alternativa4.setDisable(true);
        alternativa5.setDisable(true);
    }

    
    public static void main(String[] args) {
        launch(args);
    }
}