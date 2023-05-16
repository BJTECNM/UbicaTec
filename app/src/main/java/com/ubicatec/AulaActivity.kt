package com.ubicatec

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
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

        // Ocultar boton de la barra de estado personalizada
        val guardar = findViewById<ImageView>(R.id.btnSave)
        guardar.isVisible=false

        // Variables para manejar la información recibida de Firebase
        val lista = binding.nameAulas
        var arrayAdapter : ArrayAdapter<*>
        var nombreAulas = mutableListOf<String>()
        var idAulas = mutableListOf<String>()

        // Dialogo de progreso, para mostrar mientras no se ha terminado de recibir/procesar la info
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Cargando")
        progressDialog.setMessage("Cargando lista de salones")
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

        // Solicitud de la lista de aulas a la base de datos/Firebase
        db.collection("aulas")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    nombreAulas.add(document.data["nombreAula"].toString())
                    idAulas.add(document.id)
                }
                arrayAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,nombreAulas)
                lista.adapter = arrayAdapter
                progressDialog.dismiss()
            }
            .addOnFailureListener {
                progressDialog.dismiss()
                alertDialog!!.show()
            }

        // Identificar el elemento que se seleccione de la lista
        lista.onItemClickListener = object : AdapterView.OnItemClickListener{
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                showAula(idAulas[position])
            }
        }
    }

    // Función para iniciar la actividad donde se mostrará la info del aula seleccionada
    private fun showAula(id: String) {
        val ubicAula = Intent(this, UbicAulaActivity::class.java).apply {
            putExtra("idAula", id)
        }
        startActivity(ubicAula)
    }

}