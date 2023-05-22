package com.ubicatec

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.ubicatec.databinding.ActivityRecordatorioBinding

class RecordatorioActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val uid = Firebase.auth.currentUser!!.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRecordatorioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val save = findViewById<ImageView>(R.id.btnSave)
        val back = findViewById<ImageView>(R.id.btnBack)
        back.setOnClickListener {
            finish()
        }

        // Acción del botón guardar
        save.setOnClickListener {
            var nombre = binding.nameRecordatorio.text.toString()
            var txt = binding.txtRecordatorio.text.toString()
            if (nombre != "" && txt != ""){
                writeNewRemind(uid, nombre, txt)
                finish()
            }else{
                errorMessage()
            }

        }

    }

    private fun writeNewRemind(uid: String, id: String, texto: String) {
        val remindHashMap = hashMapOf(
            "nombreRemind" to id,
            "txt" to texto,
            //"usuario" to uid,
        )
        db.collection(uid).document(id).set(remindHashMap)
    }

    private fun errorMessage() {
        // Dialogo para mostrar en caso de error
        val alertDialog: AlertDialog? = this?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton("OK",
                    DialogInterface.OnClickListener { dialog, id ->
                        // No se hará ninguna acción especial
                    })
            }
            builder.setTitle("Error")
            builder.setMessage("Asegurate de llenar ambos campos antes de guardar el recordatorio")
            builder.create()
        }
        alertDialog!!.show()
    }
    
}