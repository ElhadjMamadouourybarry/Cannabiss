package com.cours.cannabiss

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val connect = findViewById<Button>(R.id.connect)
        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)

        connect.setOnClickListener {
            val txtEmail = email.text.toString()
            val txtPassword = password.text.toString()
            if(txtEmail.trim().isEmpty() || txtPassword.trim().isEmpty()){
                Toast.makeText(this, "Vous devez remplire tout les champs !", Toast.LENGTH_LONG).show()
            }
            else{
                val intentAcceuilActivity = Intent(this, AccueilActivity::class.java)
                startActivity(intentAcceuilActivity)

                Toast.makeText(this, "Bienvenu !", Toast.LENGTH_LONG).show()
            }

        }


    }
}