package com.example.mitfg

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.IntentSender
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.example.mitfg.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.app
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var oneTapClient : SignInClient
    private var showOneTapUI = true

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
                                    } else {
                                        Log.w(TAG, "signInWithCredential:failure", task.exception)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: LoginViewModel by viewModels()

        binding = ActivityLoginBinding.inflate(layoutInflater)
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

        binding.bLogin.setOnClickListener {

            val email = binding.tietEmail.text.toString()
            val password = binding.tietPassword.text.toString()

            signInWithEmailAndPassword(email, password)
        }


    }

    public override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        /* if (currentUser != null) {
            reload()
        } */
    }

    private fun reload() {
        TODO("Not yet implemented")
    }

    private fun signInWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail: SUCCESFUL!!")

                    val user = auth.currentUser

                    Toast.makeText(
                        baseContext,
                        "Authentication successful!!",
                        Toast.LENGTH_SHORT,
                    ).show()

                    // updateUI(user)
                } else {
                    Log.d(TAG, "signInWithEmail: FAILURE!!", task.exception)

                    Toast.makeText(
                        baseContext,
                        "Authentication failed!!",
                        Toast.LENGTH_SHORT,
                    ).show()

                    // updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        TODO("Not yet implemented")
    }
}