package com.example.bowbuddyapp.ui.game

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bowbuddyapp.databinding.FragmentSingleplayerBinding
import org.json.JSONArray
import org.json.JSONObject
import com.example.bowbuddyapp.data.Target


class SingleplayerFragment(context: Context, gameRule : String) : Fragment() {
    private var _binding: FragmentSingleplayerBinding? = null
    private val binding get() = _binding!!

    var targets = mutableListOf<Target>()
    var keyCounter = 0
    var thiscontext = context
    var thisgameRule = gameRule
    lateinit var jsonKeys : JSONArray

    var jsonStations = getParcoursMetaData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        jsonKeys= jsonStations.names()
            val targetsArr = jsonStations.getJSONArray(jsonKeys.get(keyCounter).toString())

            for (i in 0 until targetsArr.length()) {
              //targets.add(com.example.bowbuddyapp.data.Target(targetsArr.getString(i), jsonKeys.get(keyCounter).toString(), intArrayOf(0,0,0), 0))
            }
        keyCounter++


    /*
     for(key in jsonStations.keys()){

        val targetsArr = jsonStations.getJSONArray(key)

        for (i in 0 until targetsArr.length()) {
          targets.add(com.example.bowbuddyapp.game.Target(targetsArr.getString(i), key, 0))
        }
    }
     */


    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSingleplayerBinding.inflate(inflater, container, false)

        return binding.root
    }

//TODO ad previous targets for server to json
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    val gameAdapter = GameAdapter()

        binding.apply {
            viewRecyclerSingleplayer.apply {
                adapter = gameAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
            btnNextSp.setOnClickListener {
                Log.i("Test", "Click")
                targets.clear();
                gameAdapter.notifyDataSetChanged()


                jsonKeys= jsonStations.names()

                val targetsArr = jsonStations.getJSONArray(jsonKeys.get(keyCounter).toString())


                for (i in 0 until targetsArr.length()) {
                    //mutableListOf<Target>()
                    //targets.add(Target(targetsArr.getString(i), jsonKeys.get(keyCounter).toString(), intArrayOf(0,0,0), 0))

                }
                gameAdapter.notifyDataSetChanged();

                Log.i("List", targets.toString())
                keyCounter++


            }
        }
    }
//TODO replace this dummy with real call
    fun getParcoursMetaData():JSONObject {
        val jsonData = JSONObject()
        val jsonAr1 = JSONArray()
        //jsonData.put("amountOfStations", 2)
        jsonAr1.put("Waschbär")
        jsonAr1.put("Dachs")
        jsonAr1.put("Hamster")

        val jsonAr2 = JSONArray()
        jsonAr2.put("Marder")
        jsonAr2.put("Schwein")
        jsonData.put("st1",jsonAr1)
        jsonData.put("st2",jsonAr2)


    return jsonData

    }
}