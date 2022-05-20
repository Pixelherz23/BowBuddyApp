package com.example.bowbuddyapp.game

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bowbuddyapp.R
import kotlinx.android.synthetic.main.fragment_singleplayer.*
import org.json.JSONArray
import org.json.JSONObject


class SingleplayerFragment(context: Context, gameRule : String) : Fragment() {

    var targets = mutableListOf<Target>()
    val adapter = GameAdapter(targets, context, gameRule )
    var keyCounter = 0

    lateinit var jsonKeys : JSONArray

    var jsonStations = getParcoursMetaData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        jsonKeys= jsonStations.names()
            val targetsArr = jsonStations.getJSONArray(jsonKeys.get(keyCounter).toString())

            for (i in 0 until targetsArr.length()) {
              targets.add(Target(targetsArr.getString(i), jsonKeys.get(keyCounter).toString(), intArrayOf(0,0,0), 0))
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


        return inflater.inflate(R.layout.fragment_singleplayer, container, false)
    }

//TODO ad previous targets for server to json
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_recycler_singleplayer.adapter = adapter
        view_recycler_singleplayer.layoutManager = LinearLayoutManager(context)

        btn_next_sp.setOnClickListener {
            Log.i("Test", "Click")
            targets.clear();
            adapter.notifyDataSetChanged();


            jsonKeys= jsonStations.names()

            val targetsArr = jsonStations.getJSONArray(jsonKeys.get(keyCounter).toString())


            for (i in 0 until targetsArr.length()) {
                //mutableListOf<Target>()
                targets.add(Target(targetsArr.getString(i), jsonKeys.get(keyCounter).toString(), intArrayOf(0,0,0), 0))

            }
            adapter.notifyDataSetChanged();

            Log.i("List", targets.toString())
            keyCounter++


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