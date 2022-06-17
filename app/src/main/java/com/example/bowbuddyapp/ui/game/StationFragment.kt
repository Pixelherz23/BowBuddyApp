package com.example.bowbuddyapp.ui.game

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bowbuddyapp.R
import com.example.bowbuddyapp.databinding.FragmentSingleplayerBinding
import com.example.bowbuddyapp.viewModel.StationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StationFragment : Fragment() {

    private val viewModel: StationViewModel by viewModels()
    private var _binding: FragmentSingleplayerBinding? = null
    private val binding get() = _binding!!

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
        val targetAdapter = TargetAdapter(viewModel)

        viewModel.apply {
            fetchTargets(stationId)
            targets.observe(viewLifecycleOwner){ targets ->
                targetAdapter.targets = targets
                fetchPoints()

            }
            pointsTargets.observe(viewLifecycleOwner){
                it.forEachIndexed { index, points ->
                    targetAdapter.notifyItemChanged(index, points.points)
                }
            }
        }

        binding.apply {
            tvStationName.text = stationName
            tvName.text = "MaxMuster"
            viewModel.pointsStation.observe(viewLifecycleOwner){ points ->
                tvPoints.text = points.points.toString() + "P"
            }
            rvSingleplayer.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = targetAdapter

            }
        }
    }

    override fun onResume() {
        super.onResume()
        fetchPoints()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fetchPoints(){
        val stationId = arguments?.getInt(STATION_ID)!!
        val link = arguments?.getString(GAME_LINK)!!

        if(viewModel.targets.value != null){
            viewModel.apply {
                targets.value!!.forEachIndexed { index, target ->
                    fetchPoints("test@api.com", link, target.id, index).toString()
                }
                fetchPointsStation("test@api.com", link, stationId)
            }
        }
    }

    companion object{
        var STATION_ID = "id"
        var STATION_NAME = "name"
        var GAME_LINK = "link"
        @JvmStatic
        fun newInstance(id: Int, name: String, link: String) = StationFragment().apply {
            arguments = Bundle().apply {
                putInt(STATION_ID, id)
                putString(STATION_NAME, name)
                putString(GAME_LINK, link)
            }
        }
    }
}
