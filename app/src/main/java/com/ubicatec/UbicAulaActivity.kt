package com.ubicatec

import android.app.ProgressDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
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

        // Variables auxiliares para manejar la información
        val imgView1 = binding.img1
        val imgView2 = binding.img2
        val imgView3 = binding.img3
        val imgView4 = binding.img4

        // Dialogo de progreso, para mostrar mientras no se ha terminado de recibir/procesar la info
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Cargando")
        progressDialog.setMessage("Cargando datos del salón")
        progressDialog.show()

        // Dialogo para mostrar en caso de error
        val alertDialog: AlertDialog? = this?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton("OK",
                    DialogInterface.OnClickListener { dialog, id ->
                        finish()
                    })
            }
            builder.setTitle("Error")
            builder.setMessage("Error al cargar\nVerfifique su conexión a Internet y vuelva a intentarlo")
            builder.create()
        }

        // Solicitud de la info del aula a la base de datos/Firebase
        if (idAula != null) {
            db.collection("aulas").document(idAula).get().addOnSuccessListener {
                    document ->
                if (document != null) {
                    var nombre = document.data?.get("nombreAula")?.toString()
                    var txt1 = document.data!!["txt1"]?.toString()
                    var txt2 = document.data!!["txt2"]?.toString()
                    var txt3 = document.data!!["txt3"]?.toString()
                    var txt4 = document.data!!["txt4"]?.toString()
                    var img1 = document.data!!["img1"]?.toString()
                    var img2 = document.data!!["img2"]?.toString()
                    var img3 = document.data!!["img3"]?.toString()
                    var img4 = document.data!!["img4"]?.toString()

                    // Llenado de los TextView
                    binding.titulo.text = nombre
                    binding.txt1.text = txt1
                    binding.txt2.text = txt2
                    binding.txt3.text = txt3
                    binding.txt4.text = txt4

                    // Llenado de los ImageView
                    Glide.with(applicationContext)
                        .load(img1)
                        .into(imgView1)
                    Glide.with(applicationContext)
                        .load(img2)
                        .into(imgView2)
                    Glide.with(applicationContext)
                        .load(img3)
                        .into(imgView3)
                    Glide.with(applicationContext)
                        .load(img4)
                        .into(imgView4)

                    progressDialog.dismiss()

                } else {
                    progressDialog.dismiss()
                    alertDialog!!.show()
                }
            }
                .addOnFailureListener {
                    progressDialog.dismiss()
                    alertDialog!!.show()
            }

        }
    }
}