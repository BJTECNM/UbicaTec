package com.ubicatec

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.firebase.firestore.FirebaseFirestore
import com.ubicatec.databinding.ActivityAulaBinding

@Suppress("UnusedImport")
class AulaActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAulaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val guardar = findViewById<ImageView>(R.id.btnSave)
        guardar.isVisible=false

        db.collection("aulas").document("Aula 16").get().addOnSuccessListener {
            binding.aula1.setText(it.get("nombreAula")as String ?)
        }

    }

}