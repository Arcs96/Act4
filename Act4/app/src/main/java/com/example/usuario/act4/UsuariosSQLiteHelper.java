package com.example.usuario.act4;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Usuario on 18/12/2016.
 */

public class UsuariosSQLiteHelper extends SQLiteOpenHelper {

    //String sqlCreate = "CREATE TABLE Usuarios (codigo INTEGER, nombre TEXT)";

    //Sentencia SQL para crear la tabla de Usuarios
    //id_Est INT AUTO_INCREMENT
    String sqlCreate1 = "CREATE TABLE Estudiantes (id_Est integer primary key autoincrement, nombre TEXT, edad TEXT, ciclo TEXT, curso TEXT, nota TEXT);";
    String sqlCreate2 = "CREATE TABLE Profesores (id_Prof integer primary key autoincrement, nombre TEXT, edad TEXT, ciclo TEXT, curso TEXT, despacho TEXT);";

    public UsuariosSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(sqlCreate1);
        db.execSQL(sqlCreate2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS Estudiantes");
        db.execSQL("DROP TABLE IF EXISTS Profesores");

        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreate1);
        db.execSQL(sqlCreate2);
    }
}
