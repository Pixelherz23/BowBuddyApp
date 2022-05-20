package com.example.bowbuddyapp

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bowbuddyapp.googleSignIn.SignInActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*


class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


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

            findViewById<TextView>(R.id.tvGoogleName).text = acct.displayName
            findViewById<TextView>(R.id.tvGoogleEmail).text = acct.email
        }



        val homeFragment = HomeFragment(this)

        val testFragment = StatisticsFragment()



        for (i in 1..5) {
            homeFragment.initParcours.add(ParcoursCard("StaticDummy$i",
                12,
                "Musterstraße",
                "Musterhause",
                "Zusatzinfo",
                12))
        }

        //Init the container with the homeFragment
        //Isnt R.id unessacry cause kotlinx impport like for  nav_View
        //it makes a difference when you dont use "apply". Fragment wont load
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.view_fragment_Container,homeFragment)
            commit()

        }
        toggle = ActionBarDrawerToggle(this,drawer_layout,R.string.open,R.string.close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        nav_View.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.item1 ->supportFragmentManager.beginTransaction().apply {
                    replace(R.id.view_fragment_Container,homeFragment)

                    commit()

                    drawer_layout.close()
                }
                R.id.item2 ->supportFragmentManager.beginTransaction().apply {
                    replace(R.id.view_fragment_Container,testFragment)

                    commit()

                    drawer_layout.close()
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