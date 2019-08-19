package com.rotciv.marvelstore.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    public static int VERSION = 1;
    public static String NOME_DB = "DB_CARRINHO";
    public static String TABELA_CATALOGO = "catalogo";
    public static String TABELA_CARRINHO = "carrinho";

    public DbHelper(Context context) {
        super(context, NOME_DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCarrinho = "CREATE TABLE IF NOT EXISTS " + TABELA_CARRINHO + " (id INTEGER PRIMARY KEY "
                     + ", title TEXT, price REAL, raridade INTEGER); ";

        String sqlCatalogo = "CREATE TABLE IF NOT EXISTS " + TABELA_CATALOGO + " (id INTEGER PRIMARY KEY "
                     + ", title TEXT, price REAL, raridade INTEGER, url TEXT, description TEXT);";

        try {
            db.execSQL(sqlCarrinho);
            db.execSQL(sqlCatalogo);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
