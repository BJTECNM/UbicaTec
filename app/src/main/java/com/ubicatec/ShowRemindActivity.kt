package com.ubicatec

import android.app.ProgressDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.ubicatec.databinding.ActivityShowRemindBinding

class ShowRemindActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityShowRemindBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val save = findViewById<ImageView>(R.id.btnSave)
        save.isVisible=false
        val back = findViewById<ImageView>(R.id.btnBack)
        back.setOnClickListener {
            finish()
        }
        // Variables para manejar la información recibida de Firebase
        val bundle : Bundle? = intent.extras
        val idRemind : String? = bundle?.getString("idRemind")

        // Identificador del usuario
        val uid = Firebase.auth.currentUser!!.uid

        // Dialogo de progreso, para mostrar mientras no se ha terminado de recibir/procesar la info
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Cargando")
        progressDialog.setMessage("Cargando datos del recordatorio")
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

        /*
        // Solicitud de la info del aula a la base de datos/Firebase
        if (idRemind != null) {
            db.collection(uid).document(idRemind).get().addOnSuccessListener {
                    document ->
                if (document != null) {
                    binding.nameRemind.text = document.data!!["nombreRemind"]?.toString()
                    binding.txtRemind.text = document.data!!["txt"]?.toString()

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
         */
    }
}