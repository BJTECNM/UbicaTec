package com.ubicatec

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ubicatec.databinding.ActivityRecordatorioBinding

class RecordatorioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRecordatorioBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

    }
}