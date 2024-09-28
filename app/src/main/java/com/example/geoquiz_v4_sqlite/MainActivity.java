package com.example.geoquiz_v4_sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/*
  Modelo de projeto para a Atividade 1.
  Será preciso adicionar o cadastro das respostas do usuário ao Quiz, conforme
  definido no Canvas.

  GitHub: https://github.com/udofritzke/GeoQuiz
 */

public class MainActivity extends AppCompatActivity {
    private Button mBotaoVerdadeiro;
    private Button mBotaoFalso;
    private Button mBotaoProximo;
    private Button mBotaoMostra;
    private Button mBotaoDeleta;

    private Button mBotaoCola;

    private TextView mTextViewQuestao;
    private TextView mTextViewQuestoesArmazenadas;

    private static final String TAG = "QuizActivity";
    private static final String CHAVE_INDICE = "INDICE";
    private static final int CODIGO_REQUISICAO_COLA = 0;

    private Questao[] mBancoDeQuestoes = new Questao[]{
            new Questao(R.string.questao_suez, true),
            new Questao(R.string.questao_alemanha, false)
    };

    QuestaoDB mQuestoesDb;

    private int mIndiceAtual = 0;

    private boolean mEhColador;

    @Override
    protected void onCreate(Bundle instanciaSalva) {
        super.onCreate(instanciaSalva);
        setContentView(R.layout.activity_main);
        mQuestoesDb = new QuestaoDB(this);
        //Log.d(TAG, "onCreate()");
        if (instanciaSalva != null) {
            mIndiceAtual = instanciaSalva.getInt(CHAVE_INDICE, 0);
        }

        mTextViewQuestao = (TextView) findViewById(R.id.view_texto_da_questao);
        atualizaQuestao();

        mBotaoVerdadeiro = (Button) findViewById(R.id.botao_verdadeiro);
        // utilização de classe anônima interna
        mBotaoVerdadeiro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificaResposta(true);
            }
        });

        mBotaoFalso = (Button) findViewById(R.id.botao_falso);
        mBotaoFalso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificaResposta(false);
            }
        });
        mBotaoProximo = (Button) findViewById(R.id.botao_proximo);
        mBotaoProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIndiceAtual = (mIndiceAtual + 1) % mBancoDeQuestoes.length;
                mEhColador = false;
                atualizaQuestao();
            }
        });

        mBotaoCola = (Button) findViewById(R.id.botao_cola);
        mBotaoCola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // inicia ColaActivity
                // Intent intent = new Intent(MainActivity.this, ColaActivity.class);
                boolean respostaEVerdadeira = mBancoDeQuestoes[mIndiceAtual].isRespostaCorreta();
                Intent intent = ColaActivity.novoIntent(MainActivity.this, respostaEVerdadeira);
                //startActivity(intent);
                startActivityForResult(intent, CODIGO_REQUISICAO_COLA);
            }
        });

        mBotaoMostra = (Button) findViewById(R.id.botao_mostra_questoes);
        mBotaoMostra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (mQuestoesDb == null) return;
                if (mTextViewQuestoesArmazenadas == null) {
                    mTextViewQuestoesArmazenadas = (TextView) findViewById(R.id.texto_questoes_a_apresentar);
                } else {
                    mTextViewQuestoesArmazenadas.setText("");
                }

                 //Consultando a tabela Respostas
                 Cursor cursor = mQuestoesDb.queryRespostas(null, null);


                if (cursor != null) {
                    if (cursor.getCount() == 0) {
                        mTextViewQuestoesArmazenadas.setText("Nada a apresentar");

                    } else {
                        try {
                            cursor.moveToFirst();
                            while (!cursor.isAfterLast()) {

                                int id = cursor.getInt(cursor.getColumnIndex("_id")); //
                                String uuidQuestao = cursor.getString(cursor.getColumnIndex("uuid_questao"));
                                int respostaCorreta = cursor.getInt(cursor.getColumnIndex("resposta_correta"));
                                String respostaOferecida = cursor.getString(cursor.getColumnIndex("resposta_oferecida"));
                                int colou = cursor.getInt(cursor.getColumnIndex("colou"));


                                String respostaTexto = "ID: " + id +
                                        ", UUID: " + uuidQuestao +
                                        ", Correta: " + respostaCorreta +
                                        ", Oferecida: " + respostaOferecida +
                                        ", Colou: " + colou;

                                Log.i("MSGS", respostaTexto);
                                mTextViewQuestoesArmazenadas.append(respostaTexto + "\n");
                                cursor.moveToNext();
                            }
                        } finally {
                            cursor.close();
                        }
                    }
                } else {
                    Log.i("MSGS", "cursor nulo!");
                }
            }
        });


        mBotaoDeleta = (Button) findViewById(R.id.botao_deleta);
        mBotaoDeleta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        /*
          Acesso ao SQLite
        */
                if (mQuestoesDb != null) {
                    mQuestoesDb.deletaTodasAsRespostas();

                    if (mTextViewQuestoesArmazenadas == null) {
                        mTextViewQuestoesArmazenadas = (TextView) findViewById(R.id.texto_questoes_a_apresentar);
                    }
                    mTextViewQuestoesArmazenadas.setText("");
                }
            }
        });

    }

    private void atualizaQuestao() {
        int questao = mBancoDeQuestoes[mIndiceAtual].getTextoRespostaId();
        mTextViewQuestao.setText(questao);
    }

    private void verificaResposta(boolean respostaPressionada) {

        String uuidQuestao = mBancoDeQuestoes[mIndiceAtual].getId().toString();

        boolean respostaCorreta = mBancoDeQuestoes[mIndiceAtual].isRespostaCorreta();
        int idMensagemResposta = 0;

        int respostaOferecida = respostaPressionada ? 1 : 0;


        int colou = mEhColador ? 1 : 0;


        QuestoesDBHelper dbHelper = new QuestoesDBHelper(this);
        dbHelper.inserirResposta(uuidQuestao, respostaCorreta ? 1 : 0, String.valueOf(respostaOferecida), colou);

        // Mensagem de feedback ao usuário
        if (mEhColador) {
            idMensagemResposta = R.string.toast_julgamento;
        } else {
            if (respostaPressionada == respostaCorreta) {
                idMensagemResposta = R.string.toast_correto;
            } else {
                idMensagemResposta = R.string.toast_incorreto;
            }
        }

        // Mostra o toast com a mensagem
        Toast.makeText(this, idMensagemResposta, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(Bundle instanciaSalva) {
        super.onSaveInstanceState(instanciaSalva);
        Log.i(TAG, "onSaveInstanceState()");
        instanciaSalva.putInt(CHAVE_INDICE, mIndiceAtual);
    }

    @Override
    protected void onActivityResult(int codigoRequisicao, int codigoResultado, Intent dados) {
        super.onActivityResult(codigoRequisicao, codigoResultado, dados);
        if (codigoResultado != Activity.RESULT_OK) {
            return;
        }
        if (codigoRequisicao == CODIGO_REQUISICAO_COLA) {
            if (dados == null) {
                return;
            }
            mEhColador = ColaActivity.foiMostradaResposta(dados);
        }
    }
}