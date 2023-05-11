package com.ubicatec

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.core.view.isVisible
import com.google.firebase.firestore.FirebaseFirestore
import com.ubicatec.databinding.ActivityUbicAulaBinding

class UbicAulaActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityUbicAulaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle : Bundle? = intent.extras
        val idAula : String? = bundle?.getString("idAula")
        val guardar = findViewById<ImageView>(R.id.btnSave)
        guardar.isVisible=false

        if (idAula != null) {
            db.collection("aulas").document(idAula).get().addOnSuccessListener {
                    document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    binding.llenadoInfo.text=("Prueba de función. Viendo "+document.data)
                } else {
                    Log.d(TAG, "No such document")
                }
            }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)
            }
        }
    }
}