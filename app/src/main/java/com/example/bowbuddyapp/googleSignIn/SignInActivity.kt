package com.example.bowbuddyapp.googleSignIn

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.bowbuddyapp.MainActivity
import com.example.bowbuddyapp.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException


class SignInActivity : AppCompatActivity() {
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var launcher: ActivityResultLauncher<Intent?> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)


            try{
                //Do sth. with User Information
                val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } catch(e: ApiException){
                Log.e("TAG", "signInResult:failed code=" + e.statusCode)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val signInButton = findViewById<SignInButton>(R.id.sign_in_button)
        signInButton.setSize(SignInButton.SIZE_STANDARD)

        signInButton.setOnClickListener(){
            signIn()
        }

    }

    override fun onStart() {
        super.onStart()

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        val account = GoogleSignIn.getLastSignedInAccount(this)

        //Start MainActivity or LogIn
        if(account != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    //launch the login screen
    private fun signIn(){
        val intent = mGoogleSignInClient!!.signInIntent
        launcher.launch(intent)
    }
}