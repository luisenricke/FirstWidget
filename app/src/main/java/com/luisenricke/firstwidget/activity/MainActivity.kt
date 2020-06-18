package com.luisenricke.firstwidget.activity

import android.app.Dialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.Chronometer
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luisenricke.androidext.Toast
import com.luisenricke.firstwidget.R
import com.luisenricke.firstwidget.adapter.TimeAdapterX
import com.luisenricke.firstwidget.data.AppDatabase
import com.luisenricke.firstwidget.data.entity.Timer
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var database: AppDatabase

    private lateinit var txtTime: EditText
    private lateinit var txtDate: EditText
    private lateinit var txtDistance: EditText
    private lateinit var txtDescription: EditText

    private lateinit var btnStart: Button
    private lateinit var btnPause: Button
    private lateinit var btnStop: Button
    private lateinit var chronometer: Chronometer

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapterX: TimeAdapterX
    private lateinit var timers: MutableList<Timer>

    private lateinit var dialog: Dialog

    private var isPaused: Boolean = false
    private var isClicked: Boolean = false

    private var timePaused: Long = 0
    private var timeSecond: Long = 0

    private val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = AppDatabase.getInstance(this)

        btnStart = findViewById(R.id.btn_start)
        btnPause = findViewById(R.id.btn_pause)
        btnStop = findViewById(R.id.btn_stop)

        chronometer = findViewById(R.id.chronometer)

        recyclerView = findViewById(R.id.recycler_times)
        showList()

        dialog = Dialog(this)
        dialog.apply {
            setCancelable(false)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.alert_dialog_new)
        }

        btnStart.setOnClickListener(this)
        btnPause.setOnClickListener(this)
        btnStop.setOnClickListener(this)
    }

    fun showList() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        timers = database.timerDAO().get().toMutableList()

        if (timers.isEmpty()) return

        adapterX = TimeAdapterX(
            timers,
            object :
                TimeAdapterX.OnItemClickListener {
                override fun onItemClicked(item: Timer) {
                    newTime(item)
                }

            },
            database.timerDAO()
        )

        recyclerView.adapter = adapterX
    }

    fun newTime(timer: Timer?) {
        txtTime = dialog.findViewById<EditText>(R.id.txt_time)
        txtDate = dialog.findViewById<EditText>(R.id.txt_date)
        txtDistance = dialog.findViewById<EditText>(R.id.txt_distance)
        txtDescription = dialog.findViewById<EditText>(R.id.txt_description)
        timeSecond = Math.abs(timePaused / 1000)

        if (timer != null) {
            txtTime.setText(timer.time)
            txtDate.setText(timer.date)
            txtDistance.setText(timer.distance)
            txtDescription.setText(timer.description)
        } else {
            val hour = timeSecond / 3600
            val minute = (timeSecond % 3600) / 60
            val second = (timeSecond % 60)
            txtTime.setText(String.format("%02d:%02d:%02d", hour, minute, second))
            txtDate.setText(simpleDateFormat.format(Calendar.getInstance().time))
        }

        dialog.show()
        dialog.findViewById<Button>(R.id.btn_save).setOnClickListener {
            timer?.apply {
                time = txtTime.text.toString()
                date = txtDate.text.toString()
                distance = txtDescription.text.toString()
                description = txtDescription.text.toString()
            }
            if (timer?.id != null){
                database.timerDAO().update(timer)
            }else{
                database.timerDAO().insert(timer!!)
            }

            txtTime.setText("")
            txtDescription.setText("")
            txtDate.setText("")
            txtDistance.setText("")

            chronometer.base = SystemClock.elapsedRealtime()
            timePaused = 0
            dialog.dismiss()
            showList()
        }

        dialog.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            dialog.dismiss()
        }
    }

    fun Long.format(digits: Long) = "%.${digits}f".format(this)

    override fun onClick(v: View?) {
        if (v == null) return
        when (v.id) {
            R.id.btn_start -> {
                chronometer.base = SystemClock.elapsedRealtime() - timePaused
                chronometer.start()
                isPaused = false
                isClicked = true
            }
            R.id.btn_pause -> {
                if (!isPaused) {
                    timePaused = chronometer.base - SystemClock.elapsedRealtime()
                    chronometer.stop()
                    isPaused = true
                }
            }
            R.id.btn_stop -> {
                if (isClicked) {
                    if (isPaused) {
                        AlertDialog.Builder(this).setTitle("Desea")
                            .setPositiveButton(
                                "Guardar",
                                DialogInterface.OnClickListener { dialog, which ->
                                    newTime(null)
                                    dialog.dismiss()
                                })
                            .setNegativeButton(
                                "Reiniciar",
                                DialogInterface.OnClickListener { dialog, which ->
                                    chronometer.base = SystemClock.elapsedRealtime()
                                    timePaused = 0
                                    isClicked = false
                                    dialog.dismiss()
                                })
                            .show()

                    } else {
                        Toast.short(this, "El tiempo debe de estar pausado")
                    }
                } else {
                    Toast.short(this, "Iniciar el cronometro")
                }
            }
        }
    }
}
