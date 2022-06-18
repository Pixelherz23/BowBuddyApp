package com.example.bowbuddyapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bowbuddyapp.databinding.FragmentHomeBinding
import com.example.bowbuddyapp.ui.CreateParcoursActivity
import com.example.bowbuddyapp.viewModel.ParcoursViewModel
import dagger.hilt.android.AndroidEntryPoint

//Fragment(R.layout.fragment_home) does the same as on Create View

@AndroidEntryPoint
class HomeFragment() : Fragment() {

    private val viewModel: ParcoursViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var parcoursAdapter : ParcoursAdapter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

      parcoursAdapter = ParcoursAdapter(viewModel)
        binding.apply {

            viewRecycler.apply {
                adapter = parcoursAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
            btnCreateNewParcours.setOnClickListener {
                Log.i("btn", "Button for creating new parcours is clicked")
                //initParcours.add(ParcoursCard("New Parcours"))
                //adapter.notifyItemInserted(initParcours.size-1) //?? why
                val intent = Intent(context, CreateParcoursActivity::class.java)
                startActivity(intent)

            }

            viewModel.parcours.observe(viewLifecycleOwner){ parcours ->
                parcoursAdapter.parcours = parcours
                parcoursAdapter.notifyDataSetChanged()
                Log.i("PARCOUR", parcours.toString())
            }
            viewModel.pbVisibility.observe(viewLifecycleOwner){ visibility ->
                binding.progressBar.visibility = visibility
            }


        }
    }

    override fun onResume() {
        Log.i("onResume", "activated")
        super.onResume()
        parcoursAdapter.notifyDataSetChanged()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}