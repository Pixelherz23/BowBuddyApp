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

/**
 *Class for inflating the Fragment that displays all the created parcours.
 *
 *@author Kai-U. Stieler, Lukas Beckmann (co-author)
 */
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

    /**
     *Binding the the adapter to the recyler view so elements will be displayed
     * Adding functionaliy to the button for creating the parcours
     */
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
                val intent = Intent(context, CreateParcoursActivity::class.java)
                startActivity(intent)

            }
            viewModel.parcours.observe(viewLifecycleOwner){ parcours ->
                parcoursAdapter.parcours = parcours
                parcoursAdapter.notifyDataSetChanged()
                //Log.i("PARCOUR", parcours.toString())
            }
            viewModel.pbVisibility.observe(viewLifecycleOwner){ visibility ->
                binding.progressBar.visibility = visibility
            }


        }
    }
    /**
     *This is necessary in order to remove a parcours from the ui when the user deletes the parcours
     */
    override fun onResume() {
        super.onResume()
        parcoursAdapter.notifyDataSetChanged()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}