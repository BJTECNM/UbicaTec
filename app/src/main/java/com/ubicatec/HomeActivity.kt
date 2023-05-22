package com.ubicatec

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.ubicatec.databinding.ActivityHomeBinding

enum class ProviderType {
    GOOGLE
}

class HomeActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val uid = Firebase.auth.currentUser!!.uid
    private lateinit var arrayAdapter : ArrayAdapter<*>
    private var nombreRecordatorios = mutableListOf<String>()
    private var idRecordatorios = mutableListOf<String>()
    private var txtRecordatorios = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle : Bundle? = intent.extras
        val email : String? = bundle?.getString("email")
        val provider : String? = bundle?.getString("provider")

        val prefs : SharedPreferences.Editor = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email", email)
        prefs.putString("provider", provider)
        prefs.apply()

        val lista = binding.nameRecordatorios
        val swipe = binding.swipe
        val salir = findViewById<ImageView>(R.id.btnSalir)
        salir.setOnClickListener {
            prefs.clear()
            prefs.apply()

            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }

        // Recibir lista de recordatorios
        cargarDatosDB(lista, swipe)

        binding.swipe.setOnRefreshListener {
            nombreRecordatorios.clear()
            txtRecordatorios.clear()
            idRecordatorios.clear()
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
                remindMessage(nombreRecordatorios[position], txtRecordatorios[position])
            }
        }

        binding.btnRecordatorio.setOnClickListener {
            startActivity(Intent(applicationContext, RecordatorioActivity::class.java))
        }

        binding.btnAula.setOnClickListener {
            startActivity(Intent(applicationContext, AulaActivity::class.java))
        }

    }

    private fun cargarDatosDB(lista: ListView, swipe: SwipeRefreshLayout) {
        swipe.isRefreshing = true
        db.collection(uid)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    nombreRecordatorios.add(document.data["nombreRemind"].toString())
                    txtRecordatorios.add(document.data["txt"].toString())
                    idRecordatorios.add(document.id)
                }
                arrayAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,nombreRecordatorios)
                lista.adapter = arrayAdapter
                swipe.isRefreshing = false
            }
            .addOnFailureListener {
                swipe.isRefreshing = false
                errorMessage()
            }
    }

    private fun errorMessage() {
        // Dialogo para mostrar en caso de error
        val alertDialog: AlertDialog? = this?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton("OK",
                    DialogInterface.OnClickListener { dialog, id ->
                        // No se hará nada especial
                    })
            }
            builder.setTitle("Error")
            builder.setMessage("Error al cargar\nVerfifique su conexión a Internet y vuelva a intentarlo")
            builder.create()
        }
        alertDialog!!.show()
    }

    private fun remindMessage(name: String, texto: String) {
        // Dialogo para mostrar el contenido del recordatorio y poder borrarlo
        val alertDialog: AlertDialog? = this?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton("OK",
                    DialogInterface.OnClickListener { dialog, id ->
                        // Se quita el mensaje
                    })
                setNegativeButton("Borrar",
                    DialogInterface.OnClickListener{ dialog, id ->
                        // Se elimina el elemento que se está viendo
                        db.collection(uid).document(name).delete()
                    })
            }
            builder.setTitle(name)
            builder.setMessage(texto)
            builder.create()
        }
        alertDialog!!.show()
    }

}