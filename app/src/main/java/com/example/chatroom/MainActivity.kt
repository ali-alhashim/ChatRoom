package com.example.chatroom

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

import android.view.Menu
import androidx.annotation.NonNull

import com.google.android.gms.tasks.OnCompleteListener


import android.view.MenuItem
import android.widget.EditText
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    lateinit var firebaseAuth : FirebaseAuth
    var firebaseDatabase = Firebase.database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseAuth = FirebaseAuth.getInstance()



        val sendMessageButton : FloatingActionButton = findViewById(R.id.sendMessageButton)
        val input : EditText = findViewById(R.id.input)

        sendMessageButton.setOnClickListener{
            Log.d(TAG,"you clicked on send message button")

            firebaseDatabase.getReference().push().setValue(input.text.toString(), firebaseAuth.currentUser!!.displayName.toString())

            Log.d(TAG,firebaseAuth.currentUser!!.displayName.toString())

            // Clear the input
            input.setText("")
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
            displayChatMessages()

            // ...
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }


    // to add Main Menu to this Activity
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }


    // function to handle selected item from menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() === R.id.menu_sign_out)
        {
            AuthUI.getInstance().signOut(this)
                .addOnCompleteListener {
                    Toast.makeText(this@MainActivity,"You have been signed out.",Toast.LENGTH_LONG  ).show()

                    // Close activity
                    finish()
                }
        }
        return true
    }



}