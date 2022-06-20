package com.example.bowbuddyapp.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bowbuddyapp.databinding.FragmentStatisticsBinding
import com.example.bowbuddyapp.viewModel.StatisticsViewModel
import dagger.hilt.android.AndroidEntryPoint

//Fragment(R.layout.fragment_home) does the same as on Create View
/**
 * class for inflating the Statistic Fragment
 *
 *@author Kai-U. Stieler
 */
@AndroidEntryPoint
class StatisticsFragment : Fragment() {
    private val viewModel :StatisticsViewModel by viewModels()
    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!



    //view are not accessable yet cause the arent inflated yet
    //in onView Created they are accessable though
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    //called after onCreate
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     *binding the data provided by the viewmodel and addaing an obeserver to the data
     *
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapterST = StatisticsAdapter()

        viewModel.statistics.observe(viewLifecycleOwner){ statistics ->
            Log.i("SF", "observe called")
            adapterST.statistics = statistics
            binding.apply {
                viewRecyclerStatistic.apply {
                    adapter = adapterST
                    layoutManager = LinearLayoutManager(requireContext())
                }

            }


        }

        viewModel.pbVisibility.observe(viewLifecycleOwner){ visibility ->
            binding.progressBar.visibility = visibility
        }

    }



}