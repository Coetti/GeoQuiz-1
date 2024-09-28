package com.example.geoquiz_v4_sqlite;

public class Resposta {
    private String uuidQuestao;
    private int respostaCorreta; // 0 ou 1
    private boolean respostaOferecida; // verdadeiro ou falso
    private boolean colou; // se o usuário colou ou não

    public Resposta(String uuidQuestao, int respostaCorreta, boolean respostaOferecida, boolean colou) {
        this.uuidQuestao = uuidQuestao;
        this.respostaCorreta = respostaCorreta;
        this.respostaOferecida = respostaOferecida;
        this.colou = colou;
    }

    // Getters e Setters
    public String getUuidQuestao() {
        return uuidQuestao;
    }

    public int getRespostaCorreta() {
        return respostaCorreta;
    }

    public boolean isRespostaOferecida() {
        return respostaOferecida;
    }

    public boolean isColou() {
        return colou;
    }
}
