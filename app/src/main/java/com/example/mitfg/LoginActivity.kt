package com.example.mitfg

import android.content.ContentValues.TAG
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mitfg.databinding.ActivityLoginBinding
import com.example.mitfg.ui.MainActivity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var oneTapClient : SignInClient

    private val viewModel : LoginViewModel by viewModels()

    private val REQ_ONE_TAP = 2

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQ_ONE_TAP -> {
                try {
                    val googleCredential = oneTapClient.getSignInCredentialFromIntent(data)
                    val idToken = googleCredential.googleIdToken
                    when {
                        idToken != null -> {
                            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                            auth.signInWithCredential(firebaseCredential)
                                .addOnCompleteListener(this) { task ->
                                    if (task.isSuccessful) {
                                        Log.d(TAG, "signInWithCredential:success")

                                        swapToMainScreen()
                                    } else {
                                        Log.w(TAG, "signInWithCredential:failure", task.exception)

                                        val message = getString(R.string.signInWithGoogleWasNotPossible)
                                        showPopUp(message)
                                    }
                                }

                            Log.d(TAG, "Got ID token")
                        }
                        else -> {
                            Log.d(TAG, "No ID token!")
                        }
                    }
                } catch (e: ApiException) {
                    Log.w(TAG, "Google sign in failed", e)
                }
            }
        }
    }

    private fun swapToMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun swapToRegisterScreen() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        oneTapClient = Identity.getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.web_client_id))
                    .setFilterByAuthorizedAccounts(true)
                    .build())
            .build()

        // Initialize Firebase Auth
        auth = Firebase.auth

        binding.bGoogleSignIn.setOnClickListener {
            showOneTapUI()
        }

        binding.bLogin.setOnClickListener {
            if (!fieldsEmpty()) {
                val email = binding.tietEmail.text.toString()
                val password = binding.tietPassword.text.toString()

                signInWithEmailAndPassword(email, password)
            } else {
                val message = getString(R.string.emptyFieldsLoginOrRegisterMessage)

                showPopUp(message)
            }
        }

        binding.tvRegisterLink.setOnClickListener {
            swapToRegisterScreen()
        }

        Log.d("CAMPEONES", viewModel.healthAdvice.toString())

    }

    private fun showOneTapUI() {
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(this) { result ->
                try {
                    startIntentSenderForResult(
                        result.pendingIntent.intentSender, REQ_ONE_TAP,
                        null, 0, 0, 0, null)
                } catch (e: IntentSender.SendIntentException) {
                    Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            .addOnFailureListener(this) { e ->
                // No saved credentials found. Launch the One Tap sign-up flow, or
                // do nothing and continue presenting the signed-out UI.
                Log.d(TAG, e.localizedMessage)
            }
    }

    fun showCustomPopup(/* adviceDto: HealthAdviceDto */) {

        // Log.d("POPOPO", adviceDto.toString())

        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView = inflater.inflate(R.layout.popup_layout, null)

        val imageView = dialogView.findViewById<ImageView>(R.id.healthAdvice_image)
        val titleTextView = dialogView.findViewById<TextView>(R.id.healthAdvice_title)
        val descriptionTextView = dialogView.findViewById<TextView>(R.id.healthAdvice_description)

        // Personaliza el diálogo emergente con los datos proporcionados
        Glide.with(this)
            .load("https://alicorp-prod-medias.s3.amazonaws.com/static-img/files/2022-01/es_mejor_correr_por_la_manana_o_por_la_noche_/ilustracion-de-mujer-corriendo-ejercitandose-por-las-mananas.svg")
            .into(imageView)

        /* titleTextView.text = adviceDto.title
        descriptionTextView.text = adviceDto.description */

        dialogBuilder.setView(dialogView)
        dialogBuilder.setCancelable(true)

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
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

    private fun fieldsEmpty(): Boolean {
        return (binding.tietEmail.text.toString().isEmpty() ||
                binding.tietPassword.text.toString().isEmpty())
    }

    public override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            swapToMainScreen()
        }
    }

    private fun signInWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail: SUCCESFUL!!")

                    swapToMainScreen()
                } else {
                    Log.d(TAG, "signInWithEmail: FAILURE!!", task.exception)

                    val message = getString(R.string.userNotFoundMessage)

                    showPopUp(message)
                }
            }
    }
}