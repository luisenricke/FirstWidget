package com.luisenricke.firstwidget.bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by mac on 21/3/18.
 */

public class DataBaseManager {
    private DbHelper helper;
    private SQLiteDatabase sqLiteDatabase;

    public DataBaseManager(Context context){

        this.helper = DbHelper.getInstance(context);

    }

    // Cuando vamos a escribir comprueba que no sea null y que no esté abierta
    // la conexión.
    public SQLiteDatabase openWriteableDB(){
        if(sqLiteDatabase == null || !sqLiteDatabase.isOpen())
            sqLiteDatabase = this.helper.getWritableDatabase();

        return sqLiteDatabase;

    }


    // Utilizamos este método para leer en la base de datos
    public SQLiteDatabase openReableDB(){
        if(sqLiteDatabase == null || !sqLiteDatabase.isOpen())
            sqLiteDatabase = this.helper.getReadableDatabase();

        return sqLiteDatabase;

    }

    public void closeDB(SQLiteDatabase db){ // Le llamamos closeDB
        if (db != null)
            db.close();

    }

    public static String timeTable = "CREATE TABLE time(id INTEGER NOT NULL PRIMARY KEY," +
            "time TEXT(100) NOT NULL," +
            "date TEXT(100) NULL," +
            "distance TEXT(100) NULL," +
            "description TEXT(100) NULL)";






}
