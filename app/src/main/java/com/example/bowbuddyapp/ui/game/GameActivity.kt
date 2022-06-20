package com.example.bowbuddyapp.ui.game

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.bowbuddyapp.data.Station
import com.example.bowbuddyapp.databinding.ActivityGameBinding
import com.example.bowbuddyapp.viewModel.GameViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity to display the data provided by [GameViewModel]
 *
 * @author Lukas Beckmann
 */

@AndroidEntryPoint
class GameActivity : AppCompatActivity() {
    private val viewModel: GameViewModel by viewModels()
    private lateinit var binding: ActivityGameBinding


    /**
     * binding the Layout and observing the data from [GameViewModel] to update the ui.
     * Initialize and bind the [StationAdapter].
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val link = intent.getStringExtra("link")!!
        val multiplayer = intent.getBooleanExtra("multiplayer", false)

        val stationAdapter = StationAdapter(this)
        viewModel.apply {
            fetchGame(link)
            pbVisibility.observe(this@GameActivity){ visibility ->
                binding.pbStations.visibility = visibility
            }

            game.observe(this@GameActivity){game ->
                Log.i("GAME", game.link)
                stationAdapter.link = game.link
                stationAdapter.rule = game.gameRule
                stationAdapter.parcoursId = game.idParcours
                stationAdapter.multiplayer = multiplayer
                viewModel.fetchStations(game.idParcours)
            }

            stations.observe(this@GameActivity){ stations ->
                Log.i("OBSERVE", stations.toString())
                stationAdapter.stations = stations
            }
        }

        binding.viewPager.adapter = stationAdapter
    }
}