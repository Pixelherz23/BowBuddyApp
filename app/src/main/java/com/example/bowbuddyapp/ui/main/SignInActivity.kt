package com.example.bowbuddyapp.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.bowbuddyapp.data.User
import com.example.bowbuddyapp.databinding.ActivitySignInBinding
import com.example.bowbuddyapp.viewModel.SignInViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignInActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding

    private val viewModel: SignInViewModel by viewModels()


    @Inject
    lateinit var mGoogleSignInClient : GoogleSignInClient



    //private var mGoogleSignInClient: GoogleSignInClient? = null

    private var launcher: ActivityResultLauncher<Intent?> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)


            try{
                //Do sth. with User Information
                //val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
                //TODO test if this still works when you use an account that not in this recommendation chart (the chart when clicking on sign in)
                var user = User(task.result.email.toString(), task.result.displayName.toString(),0,0)
                viewModel.user.value = user
                viewModel.getUser()

                viewModel.isInDatabase.observe(this) { _ ->
                    viewModel.createUser()
                }

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)


            } catch(e: ApiException){
                Log.e("TAG", "signInResult:failed code=" + e.statusCode)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        //val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()

        // Build a GoogleSignInClient with the options specified by gso.
        //mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.apply {
            signInButton.apply {
                setSize(SignInButton.SIZE_STANDARD)
                setOnClickListener(){
                    signIn()

                }

        } }


    }

    //onStart ob du angemeldet bist
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