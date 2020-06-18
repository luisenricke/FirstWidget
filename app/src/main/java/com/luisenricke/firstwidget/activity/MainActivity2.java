package com.luisenricke.firstwidget.activity;

import android.app.AlertDialog;
import android.app.Dialog; // Este es el recurso que estamos llamando con la declaración de Dialog
import android.content.DialogInterface;
import android.os.SystemClock;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luisenricke.firstwidget.R;
import com.luisenricke.firstwidget.bd.Time;
import com.luisenricke.firstwidget.adapter.TimeAdapter;

import java.text.SimpleDateFormat; // Atentos a este
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity2 extends AppCompatActivity {

    private EditText txtTime;
    private EditText txtDate;
    private EditText txtdescrption;
    private EditText txtDistance;

    private Button btnStart;
    private Button btnPause;
    private Button btnFinalize;

    private Chronometer chronometer;

    private boolean pauseClick;
    private boolean startClick;

    // Cuidado al importar esto, porque por defecto importa otro tipo
    // En este punto del commit vamos a usar esta variable , date ,.
    private SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");

    private long timePause;

    // Vamos a crear unas variables para darle vida al método recién creado , ShowListTime ,.
    private ArrayList<Time> listTime;
    private TimeAdapter adapter;
    private RecyclerView recyclerView;

    // Declaramos una variable para el layout alertdialog_new
    private Dialog dialog;

    // Vamos a declarar una variable de tipo long
    private long timeSecond;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // region init values
        btnStart = findViewById(R.id.btnStart);
        btnFinalize = findViewById(R.id.btnFinalize);
        btnPause = findViewById(R.id.btnPause);

        // inicialiamos recyclerView aquí
        recyclerView = findViewById(R.id.rvTime);

        chronometer = findViewById(R.id.chronometer2);

        // Asignamos el valor al Dialog que hemos declarado arriba.
        dialog = new Dialog(this);
        // y ahora componemos ese Dialog
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // y por fin llamamos al layout
        dialog.setContentView(R.layout.alertdialog_new);


        // Dentro del OnCreate llamamos a nuestro método recién creado
        ShowListTime();

        Bundle data = getIntent().getExtras();
        if(data != null){
            timePause = getIntent().getExtras().getLong(WidgetDialog.TIME_PAUSE,0);
            chronometer.setBase(SystemClock.elapsedRealtime() + timePause);
            newTime(null);
        }

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                chronometer.setBase(SystemClock.elapsedRealtime() + timePause);
                chronometer.start();
                pauseClick = false;
                startClick = true;

            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!pauseClick) {
                    timePause = chronometer.getBase() - SystemClock.elapsedRealtime();
                    chronometer.stop();
                    pauseClick = true;
                }

            }
        });

        btnFinalize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (startClick) {
                    if (pauseClick) {

                        new AlertDialog.Builder(MainActivity2.this).setTitle(getString(R.string.title))
                                .setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // Si pulsamos guardar, entonces hacemos esto
                                        newTime(null); // , null , porque no queremos editar
                                        dialogInterface.dismiss();
                                    }
                                })
                                .setNegativeButton(getString(R.string.restart), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        chronometer.setBase(SystemClock.elapsedRealtime());
                                        timePause = 0;
                                        startClick = false;
                                        dialogInterface.dismiss();

                                    }
                                }).show();


                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.pause_clicked),
                                Toast.LENGTH_SHORT).show();

                    }


                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.start_clicked),
                            Toast.LENGTH_LONG).show();

                }

            }
        });

    }

    // Y por fin creamos el método para mostrar el listado
    public void ShowListTime() {

        //LLamamos al recyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false));
        // Enviamos false  aquí abajo porque
        // queremos leer
        listTime = new Time(getApplicationContext(), false).getAllTime();

        // Miramos con este if si existe algún registro en la base de datos.
        // Si existe alguno entonces lo agrega a nuestro recyclerView
        if (listTime != null) {

            adapter = new TimeAdapter(listTime, new TimeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Time time) {
                    // cuando toquemos un item nos saldrá la ventana de dialog
                    newTime(time);

                }
            });
        }
    }

    // Creamos un nuevo método para llamar al layout alertdialog_new
    public void newTime(final Time time) { // Para un new Time recibimos el Time
        // Aquí dentro mandamos a llamar a los componentes
        txtTime = dialog.findViewById(R.id.txtTime);
        txtdescrption = dialog.findViewById(R.id.txtDescription);
        txtDistance = dialog.findViewById(R.id.txtDistance);
        txtDate = dialog.findViewById(R.id.txtDates);

        // y aquí asignamos el valor de la variable recién declarada.
        timeSecond = (long) Math.abs(timePause / 1000); // dividimos entre 1000 por los milisegundos.

        // Y todoo esto nos sirve tanto para agregar como para modificar y
        // lo hacemos así
        if (time != null) { // Si esto es así tenemos que entender que estamos modificando un registro
            // y siendo así, tenemos que mostrar todos los parámetros que ya tenemos.
            txtDate.setText(time.getDate()); // Esto se entiende fácil, cogemos el time que recibimos
            txtDistance.setText(time.getDistance());
            txtdescrption.setText(time.getDescription());
            txtTime.setText(time.getTime()); // Este en concreto no va a poder modificarlo por el layout

        } else {
            // Aquí vamos a convertir el tiempo en minutos horas etc, nuevamente.
            long hours = timeSecond / 3600;
            long minutes = (timeSecond % 3600) / 60;
            long seconds = (timeSecond % 60);

            // Aquí le damos formato de horas, minutos y segundos
            txtTime.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
            // y en esta línea vamos a dar formato a la fecha
            txtDate.setText(date.format(Calendar.getInstance().getTime()));

        }
        // Y por fin se muestra
        dialog.show();
        // Cuando demos un click en btnSave, entonces pasará esto
        dialog.findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (time != null) { // Si pasa esto es que quiere editar
                    new Time(getApplicationContext(), true).update(getData(time.getId())); // true porque queremos modificar
                    // el update este de arriba pide que le pasemos un objeto Time y no sólo
                    // el , id , así que para obtener ese , id , construímos ese objeto Time
                    // aposta para luego sacarle únicamente el , id , que necesitamos.
                } else {
                    // Si no es lo anterior, entonces queremos crear un registro nuevo
                    // Le vamos a pasar un , id = 0 , pero en verdad nos da igual porque
                    // nuestra base de datos le va a asignar otro número, el número que
                    // le toque.
                    new Time(getApplicationContext(), true).save(getData(0));
                } // Esto era necesario porque las cuatro sentencias siguientes deben
                // ejecutarse tanto para modificar como para crear nuevo

                // ahora borramos los textos
                clear();
                // ponemos el cronómetro a 0
                chronometer.setBase(SystemClock.elapsedRealtime());
                timePause = 0; // ponemos esto a 0
                dialog.dismiss(); // y hacemos desaparecer el , dialog ,.
                // Y volvemos a mostrar la información actualizada.
                ShowListTime();


            }
        });

        // Vamos a dar funcionalidad al botón CANCELAR
        dialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Aquí dentro, simplemente hacemos dismiss
                dialog.dismiss();
            }
        });


    }

    // Elaboramos el método para la línea 222
    // Así construimos otro objeto Time que es llamado desde el , update , de la línea 222
    // Cogemos el id, construimos el objeto time y luego en esa línea 222 llamamos a
    // este objeto
    public Time getData(int id) {
        return new Time(id, txtTime.getText().toString(), txtDate.getText().toString(),
                txtDistance.getText().toString(), txtdescrption.getText().toString());
    }

    // CONTINUAMOS
    // Con este método limpiamos las cajas de texto
    public void clear() {
        txtDate.setText("");
        txtTime.setText("");
        txtDistance.setText("");
        txtdescrption.setText("");

    }


}
