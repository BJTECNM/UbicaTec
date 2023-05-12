package com.ubicatec

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
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
        var idAulas = mutableListOf("")
        val guardar = findViewById<ImageView>(R.id.btnSave)
        guardar.isVisible=false

        db.collection("aulas")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    nombreAulas.add(document.data["nombreAula"].toString())
                    idAulas.add(document.id)
                }
                arrayAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,nombreAulas)
                lista.adapter = arrayAdapter
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }

        lista.onItemClickListener = object : AdapterView.OnItemClickListener{
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                //Toast.makeText(applicationContext, "ID ${idAulas[position]}", Toast.LENGTH_LONG).show()
                if (idAulas[position]!=""){
                    showAula(idAulas[position])
                }
            }
        }
    }

    private fun showAula(id: String) {
        val ubicAula = Intent(this, UbicAulaActivity::class.java).apply {
            putExtra("idAula", id)
        }
        startActivity(ubicAula)
    }

}