package com.example.bowbuddyapp.ui.main

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bowbuddyapp.databinding.CustomAlertdialogJoingameBinding
import com.example.bowbuddyapp.databinding.FragmentHomeBinding
import com.example.bowbuddyapp.ui.CreateParcoursActivity
import com.example.bowbuddyapp.ui.game.GameActivity
import com.example.bowbuddyapp.viewModel.ParcoursViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

//Fragment(R.layout.fragment_home) does the same as on Create View

@AndroidEntryPoint
class HomeFragment() : Fragment() {

    private val viewModel: ParcoursViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var parcoursAdapter : ParcoursAdapter

    @Inject
    lateinit var acct : GoogleSignInAccount

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dialogBinding = CustomAlertdialogJoingameBinding.inflate(LayoutInflater.from(context))

        parcoursAdapter = ParcoursAdapter(viewModel)
        val joinGameDialog = AlertDialog.Builder(context)
            .setTitle("Wettkampf Link eingeben")
            .setPositiveButton("Beitreten"){ _, _ ->
                val link = dialogBinding.etLink.text.toString()
                viewModel.apply {
                    fetchGame(link)
                    gameExists.observe(viewLifecycleOwner){ exists ->
                        if(exists){
                            updateUser(acct.email.toString(), link)
                            val intent = Intent(context, GameActivity::class.java)
                            intent.putExtra("link", link)
                            intent.putExtra("multiplayer", true)
                            startActivity(intent)
                        }else{
                            Toast.makeText(context, "Kein Wettkampf unter diesem Link", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }
            .setNegativeButton("Abbrechen"){ _, _ ->
            }
            .setView(dialogBinding.root)
            .create()

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
            btnJoinGame.setOnClickListener{
                joinGameDialog.show()
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