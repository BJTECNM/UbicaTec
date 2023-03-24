package com.ubicatec

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.core.view.isVisible
import com.ubicatec.databinding.ActivityAulaBinding

class AulaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAulaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val guardar = findViewById<ImageView>(R.id.btnSave)
        guardar.isVisible=false

    }
}