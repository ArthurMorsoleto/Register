package com.arthurbatista.register.helper;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DBHelper extends SQLiteOpenHelper {

    public static int VERSION = 1;
    public static String DB_NAME = "DB_CLIENTES";
    public static String TB_CLIENTES = "clientes";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Criar tabela clientes
        String sql = "CREATE TABLE IF NOT EXISTS " + TB_CLIENTES +
                "  (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " nome TEXT NOT NULL," +
                " cpf TEXT NOT NULL, " +
                " cep TEXT NOT NULL," +
                " logradouro TEXT," +
                " cidade TEXT," +
                " numero TEXT," +
                " bairro TEXT," +
                " estado TEXT," +
                " nascimento TEXT ); ";
        try {
            db.execSQL(sql);
            Log.i("INFO_DB", "onCreate: Sucesso ao criar a tabela");
        } catch (SQLException e) {
            Log.i("INFO_DB", "onCreate: Erro ao criar a tabela");
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }

}
