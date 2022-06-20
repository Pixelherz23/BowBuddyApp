package com.example.bowbuddyapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.example.bowbuddyapp.R
import com.example.bowbuddyapp.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.avatarview.coil.loadImage
import javax.inject.Inject

/**
 *Entrypoint of the app. Displays the HomeFragment first and inflates the common layout of the app
 *
 *@author Kai-U. Stieler, Lukas Beckmann (co-author)
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var toggle: ActionBarDrawerToggle

    @Inject
    lateinit var mGoogleSignInClient : GoogleSignInClient

    @Inject
    lateinit var acct : GoogleSignInAccount
    /**
     *inflating basic layout and adding functionality to the navigationdrawer
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            tvGoogleName.text = acct.displayName
            tvGoogleEmail.text = acct.email
            Log.i("GOOGLE", acct.photoUrl.toString())
            avGoogleImage.loadImage(acct.photoUrl)


       

                Log.i("account data", acct.displayName.toString())
            }



        val homeFragment = HomeFragment()
        val statisticsFragment = StatisticsFragment()
        val helpFragment = HelpFragment()

        //Init the container with the homeFragment
        //Isnt R.id unessacry cause kotlinx impport like for  nav_View
        //it makes a difference when you dont use "apply". Fragment wont load
        supportFragmentManager.beginTransaction().apply {
            replace(binding.viewFragmentContainer.id ,homeFragment)
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
                    replace(R.id.view_fragment_Container,statisticsFragment)

                    commit()

                    binding.drawerLayout.close()
                }
                R.id.item4 ->supportFragmentManager.beginTransaction().apply {
                    replace(R.id.view_fragment_Container,helpFragment)

                    commit()

                    binding.drawerLayout.close()
                }
                R.id.mi_logout -> signOut(mGoogleSignInClient)


            }
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