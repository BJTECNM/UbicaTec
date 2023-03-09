package com.ubicatec

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.ubicatec.databinding.ActivityHomeBinding

enum class ProviderType {
    GOOGLE
}

class HomeActivity : AppCompatActivity() {
    val binding = ActivityHomeBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val bundle : Bundle? = intent.extras
        val email : String? = bundle?.getString("email")
        val provider : String? = bundle?.getString("provider")
        setup(email ?: "", provider ?: "")

        val prefs : SharedPreferences.Editor = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email", email)
        prefs.putString("provider", provider)
        prefs.apply()

        /*
        binding.botonCrearRemind.setOnClickListener {
            startActivity(Intent(applicationContext, CrearRecordatorio::class.java))
        }

        binding.botonDocentes.setOnClickListener {
            startActivity(Intent(applicationContext, Docente::class.java))
        }

        binding.botonAulas.setOnClickListener {
            startActivity(Intent(applicationContext, Aulas::class.java))
        }
         */

    }

    private fun setup(email: String, provider: String){
        binding.btnLogOut.setOnClickListener {
            val prefs : SharedPreferences.Editor = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()

            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }
    }
}