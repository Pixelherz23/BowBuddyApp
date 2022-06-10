package com.example.bowbuddyapp.ui.game

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.bowbuddyapp.data.Station
import com.example.bowbuddyapp.databinding.ActivityGameBinding
import com.example.bowbuddyapp.viewModel.GameViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameActivity : AppCompatActivity() {
    private val viewModel: GameViewModel by viewModels()
    private lateinit var binding: ActivityGameBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val stationAdapter = SIngleplayerAdapter()
        binding.viewPager.adapter = stationAdapter
        viewModel.apply {
            fetchGame(intent.getStringExtra("link")!!)
            pbVisibility.observe(this@GameActivity){ visibility ->
                binding.pbStations.visibility = visibility
            }

            game.observe(this@GameActivity){
                Log.i("GAME", it.link)
                viewModel.fetchStations(it.idParcours)
            }
            stations.observe(this@GameActivity){ stations ->
                Log.i("OBSERVE", stations.toString())
                stationAdapter.targets = targets
                stationAdapter.stations = stations

            }
        }
    }
}