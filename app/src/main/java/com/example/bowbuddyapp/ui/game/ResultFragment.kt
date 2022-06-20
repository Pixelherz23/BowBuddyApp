package com.example.bowbuddyapp.ui.game

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bowbuddyapp.data.Statistics
import com.example.bowbuddyapp.databinding.FragmentResultBinding
import com.example.bowbuddyapp.ui.main.MainActivity
import com.example.bowbuddyapp.viewModel.GameViewModel
import com.example.bowbuddyapp.viewModel.ResultViewModel
import com.example.bowbuddyapp.viewModel.StationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.example.bowbuddyapp.data.Game
import com.example.bowbuddyapp.data.Parcours
/**
 * Fragment to display the results provided by [ResultViewModel]
 *
 * @author Lukas Beckmann
 */
@AndroidEntryPoint
class ResultFragment: Fragment(){

    private val viewModel: ResultViewModel by viewModels()
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentResultBinding.inflate(layoutInflater)
        return binding.root
    }


    /**
     * Binding the Layout and observing the data from [ResultViewModel] to update the ui.
     * Initialize and bind the [ResultAdapter].
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val parcoursId = arguments?.getInt(PARCOURS_ID)!!
        val link = arguments?.getString(GAME_LINK)!!
        val rule = arguments?.getString(GAME_RULE)!!
        val multiplayer = arguments?.getBoolean(MULTIPLAYER)!!
        viewModel.fetchMaxTargets(parcoursId)
        val resultAdapter = ResultAdapter(viewModel, rule)
        viewModel.apply {
            fetchUser(link)

            players.observe(viewLifecycleOwner){ players ->
                resultAdapter.players = players
                fetchAllHits(link, parcoursId)

            }
            hits.observe(viewLifecycleOwner){ hits ->
                resultAdapter.hits = hits
                fetchAllPoints(link, parcoursId)
            }
            pointsParcours.observe(viewLifecycleOwner){ points ->
                points.sortByDescending { it.points }
                resultAdapter.points = points
                resultAdapter.notifyDataSetChanged()
            }
            statistics.observe(viewLifecycleOwner){ statistics ->
                statistics.forEach { email, stats ->
                    updateStatistics(email, getNewStatistics(email, rule, stats, multiplayer))
                }
                if(!statistics.isEmpty()){
                    deleteGame(link)
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                }


            }

        }

        binding.apply {
            rvResult.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = resultAdapter
            }
            btnFinishGame.setOnClickListener {
                Log.i("btn", "Button for finish game is clicked")
                viewModel.apply {
                    players.value?.forEach { player ->
                        fetchStatistics(player.email)
                    }
                }
            }
        }
    }

    /**
     * updates data on resum
     */
    override fun onResume(){
        super.onResume()
        val parcoursId = arguments?.getInt(PARCOURS_ID)!!
        val link = arguments?.getString(GAME_LINK)!!

        viewModel.apply {
            fetchAllHits(link, parcoursId)
        }
    }

    /**
     *  Provides the data which is send to the Fragment
     */
    companion object{
        var GAME_LINK = "link"
        var GAME_RULE = "rule"
        var PARCOURS_ID = "stationId"
        var MULTIPLAYER = "multiplayer"

        /**
         * Instantiated a new instance of [ResultFragment] with the given data as bundle
         * @param link the link of the [Game]
         * @param rule the rule of the [Game]
         * @param parcoursId the id of the [Parcours]
         * @param multiplayer the flag whether the game is in singleplayer or multiplayer
         */
        @JvmStatic
        fun newInstance(link: String, rule: String, parcoursId: Int, multiplayer: Boolean) = ResultFragment().apply {
            arguments = Bundle().apply {
                putString(GAME_LINK, link)
                putString(GAME_RULE, rule)
                putInt(PARCOURS_ID, parcoursId)
                putBoolean(MULTIPLAYER, multiplayer)
            }
        }
    }
}