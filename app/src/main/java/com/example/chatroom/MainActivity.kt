package com.example.chatroom

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    lateinit var firebaseAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseAuth = FirebaseAuth.getInstance()

        val sendMessageButton : FloatingActionButton = findViewById(R.id.sendMessageButton)

        sendMessageButton.setOnClickListener{
            Log.d(TAG,"you clicked on send message button")
        }


        // run Sign in Launcher
        val signInLauncher = registerForActivityResult(
            FirebaseAuthUIActivityResultContract()
        ) { res ->
            this.onSignInResult(res)
          }

        if(firebaseAuth.currentUser == null) {
            // Start sign in/sign up activity

            // Choose authentication providers
            val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.PhoneBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build(),
               // AuthUI.IdpConfig.FacebookBuilder().build(),
                AuthUI.IdpConfig.TwitterBuilder().build())

            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                //.setLogo(R.drawable.my_great_logo) // Set logo drawable
               // .setTheme(R.style.MySuperAppTheme) // Set theme
                .build()
            signInLauncher.launch(signInIntent)


        } else {
            // User is already signed in. Therefore, display
            // a welcome Toast
            Toast.makeText(this,   "Welcome " + (FirebaseAuth.getInstance().getCurrentUser()?.getDisplayName()),Toast.LENGTH_LONG).show();






            // Load chat room contents
            displayChatMessages();
        }
    }

    private fun displayChatMessages() {

    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            Toast.makeText(this,   "Welcome you Successfully signed in" + (FirebaseAuth.getInstance().getCurrentUser()?.getDisplayName()),Toast.LENGTH_LONG).show();
            // ...
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }
}