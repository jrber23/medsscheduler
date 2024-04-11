package com.example.mitfg.ui.register

import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mitfg.R
import com.example.mitfg.databinding.ActivityRegisterBinding
import com.example.mitfg.ui.main.MainActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth : FirebaseAuth

    private val viewModel: RegisterViewModel by viewModels()

    private val PASSWORD_MIN_LENGTH = 8

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        auth = Firebase.auth

        binding.bRegister.setOnClickListener {
            if (!fieldsEmpty()) {

                if (binding.tietPassword.text!!.length < PASSWORD_MIN_LENGTH) {
                    val message = getString(R.string.password_length_insufficient)

                    showPopUp(message)
                } else if (binding.tietEmail.text!!.any { it.isUpperCase() }) {
                    val message = getString(R.string.no_upper_cases_at_email)

                    showPopUp(message)
                } else {
                    val email = binding.tietEmail.text.toString()
                    val password = binding.tietPassword.text.toString()
                    val isDoctor = binding.chkBoxIsUserDoctor.isChecked

                    registerUser(email, password)

                    lifecycleScope.launch {
                        viewModel.addUser(email, password, isDoctor)
                    }
                }
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