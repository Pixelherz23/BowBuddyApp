package com.example.bowbuddyapp.ui.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bowbuddyapp.data.Game
import com.example.bowbuddyapp.data.Parcours
import com.example.bowbuddyapp.databinding.FragmentSingleplayerBinding
import com.example.bowbuddyapp.viewModel.ResultViewModel
import com.example.bowbuddyapp.viewModel.StationViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.example.bowbuddyapp.data.User

/**
 * Fragment to display the [Target]s and [User]s of a game provided by [StationViewModel]
 *
 * @author Lukas Beckmann
 */
@AndroidEntryPoint
class StationFragment : Fragment() {

    private val viewModel: StationViewModel by viewModels()
    private var _binding: FragmentSingleplayerBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var acct : GoogleSignInAccount

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSingleplayerBinding.inflate(layoutInflater)
        return binding.root
    }


    /**
     * Binding the Layout and observing the data from [StationViewModel] to update the ui.
     * Initialize and bind the [TargetAdapter] and [PlayerAdapter].
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val stationId = arguments?.getInt(STATION_ID)!!
        val stationName = arguments?.getString(STATION_NAME)!!
        val link = arguments?.getString(GAME_LINK)!!
        val rule = arguments?.getString(GAME_RULE)!!

        val targetAdapter = TargetAdapter(viewModel, rule, link)
        val playerAdapter = PlayerAdapter()

        viewModel.apply {
            fetchTargets(stationId)
            fetchUser(link)

            targets.observe(viewLifecycleOwner){ targets ->
                targetAdapter.targets = targets
                fetchAllPoints(link, acct.email!!)
            }
            player.observe(viewLifecycleOwner){ player ->
                playerAdapter.players = player
                fetchAllPoints(link, stationId)
            }
            pointsTargets.observe(viewLifecycleOwner){ pointsList ->
                pointsList.forEachIndexed { index, points ->
                    targetAdapter.notifyItemChanged(index, points.points)
                }
                fetchAllPoints(link, stationId)
            }
            pointsStation.observe(viewLifecycleOwner){ pointsList ->
                pointsList.forEachIndexed { index, points ->
                    playerAdapter.notifyItemChanged(index, points.points)
                }
            }
        }

        binding.apply {
            tvStationName.text = stationName
            rvPlayer.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = playerAdapter
            }
            rvSingleplayer.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = targetAdapter
            }
        }
    }

    /**
     * updates data on resume
     */
    override fun onResume() {
        super.onResume()
        val stationId = arguments?.getInt(STATION_ID)!!
        val link = arguments?.getString(GAME_LINK)!!

        viewModel.apply {
            fetchUser(link)
            fetchAllPoints(link, acct.email!!)
            fetchAllPoints(link, stationId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     *  Provides the data which is send to the Fragment
     */
    companion object{
        var STATION_ID = "id"
        var STATION_NAME = "name"
        var GAME_LINK = "link"
        var GAME_RULE = "rule"
        /**
         * Instantiate a new instance of [StationFragment] with the given data as bundle
         * @param id the id of the [Station]
         * @param name the name of the [Station]
         * @param link the link of the [Game]
         * @param rule the rule of the [Game]
         */
        @JvmStatic
        fun newInstance(id: Int, name: String, link: String, rule: String) = StationFragment().apply {
            arguments = Bundle().apply {
                putInt(STATION_ID, id)
                putString(STATION_NAME, name)
                putString(GAME_LINK, link)
                putString(GAME_RULE, rule)
            }
        }
    }
}
