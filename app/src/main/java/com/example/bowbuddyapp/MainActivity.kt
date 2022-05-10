package com.example.bowbuddyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*


class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val homeFragment = HomeFragment(this)

        val testFragment = StatisticsFragment()

        homeFragment.initParcours.add(ParcoursCard("StaticDummy",
            12,
            "Musterstraße",
            "Musterhause",
            "Zusatzinfo",
            12))

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
}