package com.example.geoquiz_v4_sqlite;

public class Resposta {
    private String uuidQuestao;
    private int respostaCorreta;
    private boolean respostaOferecida;
    private boolean colou;

    public Resposta(String uuidQuestao, int respostaCorreta, boolean respostaOferecida, boolean colou) {
        this.uuidQuestao = uuidQuestao;
        this.respostaCorreta = respostaCorreta;
        this.respostaOferecida = respostaOferecida;
        this.colou = colou;
    }

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
