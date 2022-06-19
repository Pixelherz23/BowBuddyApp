package com.example.bowbuddyapp.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.bowbuddyapp.databinding.FragmentResultBinding
import com.example.bowbuddyapp.viewModel.ResultViewModel
import com.example.bowbuddyapp.viewModel.StationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultFragment: Fragment(){

    private val viewModel: StationViewModel by viewModels()
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentResultBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    companion object{
        var GAME_LINK = "link"
        var GAME_RULE = "rule"
        @JvmStatic
        fun newInstance(link: String, rule: String) = ResultFragment().apply {
            arguments = Bundle().apply {
                putString(StationFragment.GAME_LINK, link)
                putString(StationFragment.GAME_RULE, rule)
            }
        }
    }
}