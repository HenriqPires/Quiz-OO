package aulaoo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class GerenciarPerguntas {

    private final String caminhoArquivo;

    public GerenciarPerguntas(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }

    //salvar uma nova pergunta no arquivo
    public void salvarPergunta(Questao questao) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo, true))) {
            writer.write(questao.getEnunciado() + "\n");
            writer.write(questao.getRespostaCorreta() + "\n");

            for (String alternativa : questao.getAlternativas()) {
                writer.write(alternativa + "\n");
                
            }

            writer.write("\n"); // Adiciona uma linha em branco para separar as perguntas
        }
    }

    //carregar perguntas do arquivo
    public ArrayList<Questao> carregarPerguntas() throws IOException {
        ArrayList<Questao> perguntas = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String enunciado = linha;
                String respostaCorreta = reader.readLine();
                String[] alternativas = new String[5];

                for (int i = 0; i < 5; i++) {
                    alternativas[i] = reader.readLine();
                }

                perguntas.add(new Questao(enunciado, respostaCorreta, alternativas));
                reader.readLine(); // pular linha em branco entre perguntas
            }
        }
        return perguntas;
    }

    
}
