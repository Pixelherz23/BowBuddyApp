package com.example.bowbuddyapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.example.bowbuddyapp.R
import com.example.bowbuddyapp.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestProfile().build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        val acct = GoogleSignIn.getLastSignedInAccount(this)

        if(acct != null){
            if(acct.photoUrl != null){
                findViewById<ImageView>(R.id.ivGoogleImage).setImageURI(null)
                findViewById<ImageView>(R.id.ivGoogleImage).setImageURI(acct.photoUrl)

            }else{
                Toast.makeText(this, "No Image", Toast.LENGTH_SHORT).show()
            }
            binding.apply {
                tvGoogleName.text = acct.displayName
                tvGoogleEmail.text = acct.email
            }

        }



        val homeFragment = HomeFragment()

        val testFragment = StatisticsFragment()

        //Init the container with the homeFragment
        //Isnt R.id unessacry cause kotlinx impport like for  nav_View
        //it makes a difference when you dont use "apply". Fragment wont load
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.view_fragment_Container,homeFragment)
            commit()

        }
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.item1 ->supportFragmentManager.beginTransaction().apply {
                    replace(R.id.view_fragment_Container,homeFragment)

                    commit()

                    binding.drawerLayout.close()
                }
                R.id.item2 ->supportFragmentManager.beginTransaction().apply {
                    replace(R.id.view_fragment_Container,testFragment)

                    commit()

                    binding.drawerLayout.close()
                }
                R.id.mi_logout -> signOut(mGoogleSignInClient)


            }
            //why true though
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    fun signOut(client: GoogleSignInClient){
        client.signOut().addOnCompleteListener(this, object: OnCompleteListener<Void> {
            override fun onComplete(p0: Task<Void>) {
                Toast.makeText(this@MainActivity, "Signed Out", Toast.LENGTH_LONG).show()
            }
        })
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }


}