package com.ubicatec

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.ubicatec.databinding.ActivityUbicAulaBinding

class UbicAulaActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityUbicAulaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ocultar boton de la barra de estado personalizada
        val guardar = findViewById<ImageView>(R.id.btnSave)
        guardar.isVisible=false

        // Variables para manejar la información recibida de Firebase
        val bundle : Bundle? = intent.extras
        val idAula : String? = bundle?.getString("idAula")
        val imageView = binding.imgDescripcion

        // Dialogo de progreso, para mostrar mientras no se ha terminado de recibir/procesar la info
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Cargando")
        progressDialog.setMessage("Cargando datos del salón")
        progressDialog.show()

        // Solicitud de la info del aula a la base de datos/Firebase
        if (idAula != null) {
            db.collection("aulas").document(idAula).get().addOnSuccessListener {
                    document ->
                if (document != null) {
                    progressDialog.dismiss()
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    binding.titulo.text = (document.data!!["nombreAula"].toString())
                    binding.llenadoInfo.text = ("Descripción: ${document.data!!["descripcion"]}")
                    var aux = document.data!!["imgDescripcion"].toString()
                    Glide.with(applicationContext)
                        .load(aux)
                        .into(imageView)
                } else {
                    progressDialog.dismiss()
                    Log.d(TAG, "No such document")
                }
            }
                .addOnFailureListener { exception ->
                    progressDialog.dismiss()
                    Log.d(TAG, "get failed with ", exception)
            }
        }
    }
}