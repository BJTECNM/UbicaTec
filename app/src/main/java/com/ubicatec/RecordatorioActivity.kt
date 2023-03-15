package com.ubicatec

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.ubicatec.databinding.ActivityRecordatorioBinding

class RecordatorioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRecordatorioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val save = findViewById<ImageView>(R.id.btnSave)
        val back = findViewById<ImageView>(R.id.btnBack)

        // Falta implementar sistema de recordatorios
        save.setOnClickListener {
            volver()
        }

        back.setOnClickListener {
            volver()
        }
        /*
        val fecha = binding.txtFecha
        val hora = binding.txtHora
        fecha.isEnabled = false
        hora.isEnabled = false

        val switchAlarma = binding.switchAlarma
        switchAlarma.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                fecha.isEnabled = true
                hora.isEnabled = true
            } else {
                fecha.isEnabled = false
                hora.isEnabled = false
            }
        }
         */

    }

    private fun volver (){
        finish()
    }
}