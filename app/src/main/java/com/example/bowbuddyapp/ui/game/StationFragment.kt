package com.example.bowbuddyapp.ui.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bowbuddyapp.databinding.FragmentSingleplayerBinding
import com.example.bowbuddyapp.viewModel.StationViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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
            pointsTargets.observe(viewLifecycleOwner){
                it.forEachIndexed { index, points ->
                    targetAdapter.notifyItemChanged(index, points.points)
                }
            }
            pointsStation.observe(viewLifecycleOwner){
                it.forEachIndexed { index, points ->
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

    override fun onResume() {
        super.onResume()
        val stationId = arguments?.getInt(STATION_ID)!!
        val link = arguments?.getString(GAME_LINK)!!

        viewModel.apply {
            fetchAllPoints(link, acct.email!!)
            fetchAllPoints(link, stationId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        var STATION_ID = "id"
        var STATION_NAME = "name"
        var GAME_LINK = "link"
        var GAME_RULE = "rule"
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
