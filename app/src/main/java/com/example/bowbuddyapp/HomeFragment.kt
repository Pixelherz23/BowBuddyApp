package com.example.bowbuddyapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*

//Fragment(R.layout.fragment_home) does the same as on Create View
class HomeFragment : Fragment() {

    var initParcours = mutableListOf<ParcoursCard>()
    val adapter = ParcoursAdapter(initParcours)



    //view are not accessable yet cause the arent inflated yet
    //in onView Created they are accessable though
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        view_recycler.adapter = adapter
        view_recycler.layoutManager = LinearLayoutManager(context)


        //initParcours.add(ParcoursCard("Bogenbahn1"))

        btn_create_new_parcours.setOnClickListener {
            Log.i("btn", "Button for creating new parcours is clicked")
            //initParcours.add(ParcoursCard("New Parcours"))
            //adapter.notifyItemInserted(initParcours.size-1) //?? why
            val intent = Intent(context, CreateParcoursActivity::class.java)
            startActivity(intent)

        }



    }


}