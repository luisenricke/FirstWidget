package com.luisenricke.firstwidget.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by mac on 22/3/18.
 */

public class Time {

    private int id;
    private String time;
    private String date;
    private String distance;
    private String description;

    private static DataBaseManager dataBaseManager;
    private static SQLiteDatabase database;

    // Añadimos otra estática con el nombre de la tabla para no tener errores en las
    // líneas que estamos añadiendo abajo
    private static final String table = "time";

    public Time(){

        id = 0;
        time = "";
        date = "";
        distance = "";
        description = "";

    }

    public Time(int id, String time, String date, String distance, String description) {
        this.id = id;
        this.time = time;
        this.date = date;
        this.distance = distance;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Time(Context context, boolean write){

        // Agregamos el contexto a la base de datos para poder usarla
        dataBaseManager = new DataBaseManager(context);
        if(write) // Si es true es que quiero escribir
            database = dataBaseManager.openWriteableDB();
        else // Si es false entonces lo que quiero es sólo leer
            database = dataBaseManager.openReableDB();

    }

    // Añadimos un método para leer todos los registros que hay en la
    // base de datos.
    public ArrayList<Time> getAllTime(){
        try {
            ArrayList<Time> allTime = new ArrayList<>();
            // elegimos todoo
            Cursor cursor = database.rawQuery("select * from " + table, null);
            // extraemos un cursor de toda la tabla
            while (cursor.moveToNext()){
                Time time = new Time(cursor.getInt(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4));
                allTime.add(time);
            }
            // Una vez extraídos todos los registros, cerramos la llamada.
            dataBaseManager.closeDB(database); // Estaba mal, ahora está bien.
            // Y retornamos el ArrayList<Time>, de nombre allTime
            return allTime;

        } catch (Exception ex){
            Log.e("Time/getAllTime", ex.toString());
            return null;
        }
    }

    // Vamos a extraer sólo un registro
    public Time getTime(int id){ // le pedimos el id para que identifique el registro
        try {

            Cursor cursor = database.rawQuery("select * from " + table +
                    "where id='"+id+"'", null);
            while (cursor.moveToFirst()){ // move to first para que nos seleccione sólo el primero
                return new Time(cursor.getInt(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4));
            }
            dataBaseManager.closeDB(database); // Esto cierra la conexión a SQLite
        } catch (Exception e){
            Log.e("Time/getAllTime:", e.toString());
            return null;
        }
        return null;
    }

    // Salvar un registro
    public boolean save(Time time){

        try {
            ContentValues values = new ContentValues();
            values.put("time", time.getTime());
            values.put("date", time.getDate());
            values.put("distance", time.getDistance());
            values.put("description", time.getDescription());
            // Si ocurre lo siguiente se confirma la inserción que nos
            // devuelve un número entero, de lo contrario
            // nos devolvería un , 0 ,.
            boolean result = database.insert(table, null, values)!=0;
            // Ahora cerramos la conexión
            dataBaseManager.closeDB(database);
            return result;


        } catch(Exception e) {
            Log.e("Time/save:", e.toString());
            return false; // Aquí retornamos un false

        }

    }

    // Vamos a crear la manera de borrar registros
    public boolean delete(int id){

        try {

            boolean result = database.delete(table,"id='"+id+"'",null)!=0;
            dataBaseManager.closeDB(database);
            return result;

        } catch(Exception e) {
            Log.e("Time/delete:", e.toString());
            return false; // Aquí retornamos un false

        }

    }
    // Aquí para modificar registros
    public boolean update(Time time){

        try {
            ContentValues values = new ContentValues();
            values.put("time", time.getTime());
            values.put("date", time.getDate());
            values.put("distance", time.getDistance());
            values.put("description", time.getDescription());
            // Si ocurre lo siguiente se confirma la inserción que nos
            // devuelve un número entero, de lo contrario
            // nos devolvería un , 0 ,.
            boolean result = database.update(table, values, "id='"+time.getId()+"'", null)!=0;
            // Ahora cerramos la conexión
            dataBaseManager.closeDB(database);
            return result;


        } catch(Exception e) {
            Log.e("Time/update:", e.toString());
            return false; // Aquí retornamos un false

        }


    }





}









