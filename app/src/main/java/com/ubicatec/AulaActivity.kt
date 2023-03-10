package com.ubicatec

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ubicatec.databinding.ActivityAulaBinding

class AulaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAulaBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}