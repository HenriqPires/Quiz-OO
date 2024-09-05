package aulaoo;

import java.util.ArrayList;
import java.util.Collections;


public class Questao {

    private final String enunciado;
    private final String respostaCorreta;
    private final ArrayList<String> alternativas;

    public Questao(String enunciado, String respostaCorreta, String[] alternativas) {
        this.enunciado = enunciado;
        this.respostaCorreta = respostaCorreta;
        this.alternativas = new ArrayList<>();
        Collections.addAll(this.alternativas, alternativas);

        // verifica se a resposta correta já está na lista de alternativas
        if (!this.alternativas.contains(respostaCorreta)) {
            this.alternativas.add(respostaCorreta);
        }
        
        embaralharAlternativas();
    }

    public String getEnunciado() {
        return enunciado;
    }

    public String getRespostaCorreta() {
        return respostaCorreta;
    }

    public ArrayList<String> getAlternativas() {
        return alternativas;
    }

    public void embaralharAlternativas() {
        Collections.shuffle(alternativas);
    }

    public ArrayList<String> getTodasAlternativas() {
        return new ArrayList<>(alternativas);
    }
}