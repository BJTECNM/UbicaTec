package com.ubicatec

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
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

        val lista = binding.nameAulas
        var arrayAdapter : ArrayAdapter<*>
        var nombreAulas = mutableListOf("")
        val guardar = findViewById<ImageView>(R.id.btnSave)
        guardar.isVisible=false

        db.collection("aulas")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    nombreAulas.add(document.data["nombreAula"].toString())
                }
                arrayAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,nombreAulas)
                lista.adapter = arrayAdapter
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }

    }

}