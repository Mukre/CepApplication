package com.teste.crudapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Crud.db";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS tb_cep(id INTEGER PRIMARY KEY AUTOINCREMENT , cep CHAR(8), logradouro CHAR(30)," +
            " complemento CHAR(30), bairro CHAR(30), localidade CHAR(30), uf CHAR(30), ibge CHAR(30), gia CHAR(30), ddd CHAR(2), siafi CHAR(30));";
    public DbHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
