package com.example.bowbuddyapp.ui.game

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bowbuddyapp.R
import com.example.bowbuddyapp.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
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