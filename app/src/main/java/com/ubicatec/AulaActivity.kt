package com.ubicatec

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.ubicatec.databinding.ActivityAulaBinding

@Suppress("UnusedImport")
class AulaActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var arrayAdapter : ArrayAdapter<*>
    private var nombreAulas = mutableListOf<String>()
    private var idAulas = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAulaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val lista = binding.nameAulas
        val swipe = binding.swipeAula
        val save = findViewById<ImageView>(R.id.btnSave)
        save.isVisible=false
        val back = findViewById<ImageView>(R.id.btnBack)
        back.setOnClickListener {
            finish()
        }

        cargarDatosDB(lista, swipe)

        binding.swipeAula.setOnRefreshListener {
            nombreAulas.clear()
            idAulas.clear()
            cargarDatosDB(lista, swipe)
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

    // Función para cargar la lista de aulas de la base de datos/Firebase
    private fun cargarDatosDB(lista: ListView, swipe: SwipeRefreshLayout) {
        swipe.isRefreshing = true
        db.collection("aulas")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    nombreAulas.add(document.data["nombreAula"].toString())
                    idAulas.add(document.id)
                }
                arrayAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,nombreAulas)
                lista.adapter = arrayAdapter
                swipe.isRefreshing = false
            }
            .addOnFailureListener {
                swipe.isRefreshing = false
                errorMessage()
            }
    }

    // Función para iniciar la actividad donde se mostrará la info del aula seleccionada
    private fun showAula(id: String) {
        val ubicAula = Intent(this, UbicAulaActivity::class.java).apply {
            putExtra("idAula", id)
        }
        startActivity(ubicAula)
    }

    private fun errorMessage() {
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
        alertDialog!!.show()
    }
}