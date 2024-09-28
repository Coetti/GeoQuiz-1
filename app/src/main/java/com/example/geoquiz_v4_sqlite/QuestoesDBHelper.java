package com.example.geoquiz_v4_sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class QuestoesDBHelper extends SQLiteOpenHelper {
    private static final int VERSAO = 4;
    private static final String NOME_DATABASE = "questoesDB";

    public QuestoesDBHelper(Context context) {
        super(context, NOME_DATABASE, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + QuestoesDbSchema.QuestoesTbl.NOME + " (" +
                "_id integer PRIMARY KEY autoincrement, " +
                QuestoesDbSchema.QuestoesTbl.Cols.UUID + ", " +
                QuestoesDbSchema.QuestoesTbl.Cols.QUESTAO_CORRETA + ", " +
                QuestoesDbSchema.QuestoesTbl.Cols.TEXTO_QUESTAO + ")");

        db.execSQL("CREATE TABLE Respostas (" +
                "_id integer PRIMARY KEY autoincrement, " +
                "uuid_questao TEXT NOT NULL, " +
                "resposta_correta INTEGER NOT NULL, " + // 0 ou 1
                "resposta_oferecida TEXT NOT NULL, " +  // "verdadeiro" ou "falso"
                "colou INTEGER NOT NULL);"); // 0 ou 1
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versaoAntiga, int novaVersao) {
            // Política de upgrade é simplesmente descartar o conteúdo e começar novamente
            db.execSQL("DROP TABLE IF EXISTS " + QuestoesDbSchema.QuestoesTbl.NOME);
            onCreate(db);
    }

    public void inserirResposta(String uuidQuestao, int respostaCorreta, String respostaOferecida, int colou) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("uuid_questao", uuidQuestao);
        values.put("resposta_correta", respostaCorreta);
        values.put("resposta_oferecida", respostaOferecida);
        values.put("colou", colou);

        long newRowId = db.insert("Respostas", null, values);
        db.close();
    }

    public Cursor queryRespostas(String selection, String[] selectionArgs) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query("Respostas", null, selection, selectionArgs, null, null, null);
    }

    public void removeTodasRespostas() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Respostas", null, null); // Deleta todas as entradas na tabela Respostas
        db.close();
    }
}