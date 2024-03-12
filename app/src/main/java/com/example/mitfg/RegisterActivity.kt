package com.example.mitfg

import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mitfg.databinding.ActivityRegisterBinding
import com.example.mitfg.ui.MainActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        auth = Firebase.auth

        binding.bRegister.setOnClickListener {
            if (!fieldsEmpty()) {
                val email = binding.tietEmail.text.toString()
                val password = binding.tietPassword.text.toString()

                registerUser(email, password)
            } else {
                val message = getString(R.string.emptyFieldsLoginOrRegisterMessage)

                showPopUp(message)
            }
        }
    }

    private fun fieldsEmpty(): Boolean {
        return (binding.tietEmail.text.toString().isEmpty() ||
                binding.tietPassword.text.toString().isEmpty())
    }

    fun showPopUp(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton(R.string.accept, DialogInterface.OnClickListener { dialog, _ ->
                dialog.dismiss()
            })
        val alert = builder.create()
        alert.show()
    }

    private fun swapToMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    swapToMainScreen()

                    Log.d(TAG, "User created successfully!!")
                } else {
                    val message = getString(R.string.registerFail)
                    showPopUp(message)

                    Log.d(TAG, "Something went wrong")
                }
            }
    }
}