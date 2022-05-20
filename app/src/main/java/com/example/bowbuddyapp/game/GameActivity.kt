package com.example.bowbuddyapp.game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bowbuddyapp.R

class GameActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        val singleplayerFragment = SingleplayerFragment(this, intent.getStringExtra("gameRule").toString())
        val multiplayerFragment = MultiplayerFragment()
        //TODO replace static dummy
        //singleplayerFragment.targets.add(Target("test","szt1", 0))
        //singleplayerFragment.targets.add(Target("test2","szt1", 0))


        if(intent.getBooleanExtra("isMultiplayer", false) == false){
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.view_game_fragment_Container,singleplayerFragment)
                commit()

            }
        }else{
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.view_game_fragment_Container,multiplayerFragment)
                commit()

            }
        }



    }

}