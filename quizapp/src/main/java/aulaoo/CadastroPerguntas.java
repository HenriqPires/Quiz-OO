package aulaoo;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import java.io.IOException;

public class CadastroPerguntas {

    private final Stage stage;
    private final App app;

    public CadastroPerguntas(Stage stage, App app) {
        this.stage = stage;
        this.app = app;
    }

    public void mostrar() {
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("tela-cadastro");

        Label enunciadoLabel = new Label("Enunciado da Pergunta:");
        enunciadoLabel.getStyleClass().add("label-cadastro"); 
        TextField enunciadoField = new TextField();
        enunciadoField.getStyleClass().add("text-field-cadastro");


        Label respostaLabel = new Label("Resposta Correta:");
        respostaLabel.getStyleClass().add("label-cadastro"); 
        TextField respostaField = new TextField();
        respostaField.getStyleClass().add("text-field-cadastro"); 


        Label alternativasLabel = new Label("Outras Alternativas (separadas por vírgula):");
        alternativasLabel.getStyleClass().add("label-cadastro"); 
        TextField alternativasField = new TextField();
        alternativasField.getStyleClass().add("text-field-cadastro"); 


        Button salvarButton = new Button("Salvar Pergunta");
        salvarButton.getStyleClass().add("botao-cadastro"); 
        salvarButton.setOnAction(e -> {
            boolean sucesso = salvarPergunta(enunciadoField.getText(), respostaField.getText(), alternativasField.getText());
            if (sucesso) {
                app.mostrarTelaInicial("Pergunta cadastrada com sucesso!"); 
            } else {
                //exibir uma mensagem de erro, se necessário
                System.out.println("Falha ao salvar a pergunta.");
            }
        });

        Button voltarButton = new Button("Voltar");
        voltarButton.getStyleClass().add("botao-voltar");
        voltarButton.setOnAction(e -> voltarParaTelaInicial());

        root.getChildren().addAll(enunciadoLabel, enunciadoField, respostaLabel, respostaField, alternativasLabel, alternativasField, salvarButton, voltarButton);

        Scene scene = new Scene(root, 400, 300);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Cadastro de Perguntas");
        stage.show();
    }

    private boolean salvarPergunta(String enunciado, String respostaCorreta, String alternativas) {
        GerenciarPerguntas gerenciador = new GerenciarPerguntas("ArmazenarPerguntas.txt");
        String[] alternativasArray = alternativas.split(",");
       
        if (alternativasArray.length > 4) {
            System.out.println("Erro: O número de alternativas deve ser no máximo 4.");
            return false;
        }
    
        Questao novaQuestao = new Questao(enunciado, respostaCorreta, alternativasArray);
        
        try {
            gerenciador.salvarPergunta(novaQuestao);
            return true; 
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void voltarParaTelaInicial() {
        app.mostrarTelaInicial(); //atualiza para chamar a tela inicial correta
    }
}
