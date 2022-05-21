package com.example.bowbuddyapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.core.view.size
import com.example.bowbuddyapp.R
import com.example.bowbuddyapp.databinding.ActivityStationSetupBinding
import com.example.bowbuddyapp.ui.main.MainActivity
import org.json.JSONArray
import org.json.JSONObject


//Todo
// fix bug at addOnChangeListener. Inserts acting weird [DONE]
// send targets stations and Parcoursinfos to API

class StationSetupActivity : AppCompatActivity() {
    //val BASE_URL = "https://dummy.restapiexample.com"
    val BASE_URL = "https://59baf216-74eb-4959-9c0c-f38ed4849c5b.mock.pstmn.io"
    private lateinit var binding: ActivityStationSetupBinding

    var amountOfStations = 0
    var stationCounter = 1
    var tempSliderCounter = 1
    lateinit var jsonParcours: JSONObject

    val idList = mutableListOf(
        R.id.et_target1,
        R.id.et_target2,
        R.id.et_target3,
        R.id.et_target4,
        R.id.et_target5
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStationSetupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        amountOfStations = intent.getIntExtra("amountOfStations", 1)
        binding.etStatinNo.setText("Station $stationCounter von $amountOfStations")
        jsonParcours = JSONObject(intent.getStringExtra("json"))

            var a = EditText(applicationContext)
            a.hint = "Zielbezeichnung"
            //a.id = idList[0]
            binding.linearLayoutTargets.addView(a)

        if(amountOfStations == stationCounter){
            binding.btnNextStation.setText("Speichern")

        }



        //If you only want the slider start and end value and don't care about the previous values
        binding.slider.addOnChangeListener { slider, value, fromUser ->
            //val values = slider.value.toString()
            Log.i("Slider Val", value.toString())
            var sliderVal = value.toInt()
            var from = slider.valueFrom.toInt()
            var to = slider.valueTo.toInt()

            if (tempSliderCounter < sliderVal) {

                var additionalViews = sliderVal - tempSliderCounter

                for (tempVal in 1..additionalViews) {
                    var temp = EditText(applicationContext)
                    temp.hint = "Zielbezeichnung"
                    //temp.id = idList[0]
                    binding.linearLayoutTargets.addView(temp)
                }
                //linearLayout_Targets.addView(EditText(applicationContext))

                tempSliderCounter = sliderVal

            } else if (tempSliderCounter >= sliderVal) {
                var removeViews = tempSliderCounter - sliderVal
                var x = binding.linearLayoutTargets.size - 1
                for (tempVal in 1..removeViews) {

                    binding.linearLayoutTargets.removeViewAt(x)
                    x--
                }

                tempSliderCounter = sliderVal
            }

        }

        binding.btnNextStation.setOnClickListener {
            var flag = false

            binding.linearLayoutTargets.forEach {

                if (it is EditText) {
                    if (it.text.isEmpty()) {
                        flag = true
                    }
                }

            }

            if (flag == true) {
                Toast.makeText(applicationContext, "Alle Felder ausfüllen", Toast.LENGTH_SHORT)
                    .show()

            } else {
                /*
                if (stationCounter < amountOfStations) {

                    saveInputAndClearFields(jsonParcours)

                    stationCounter++
                    //Log.i("counter", stationCounter.toString())
                    et_statinNo.setText("Station $stationCounter von $amountOfStations")

                }
                if(stationCounter == amountOfStations){
                   et_statinNo.set

                }
                 */

                if (stationCounter < amountOfStations) {

                    saveInputAndClearFields(jsonParcours)

                }
                if(stationCounter == amountOfStations) {
                    saveInputAndClearFields(jsonParcours)

                    sendJsonToServer(jsonParcours)
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                }
                if(amountOfStations ==( stationCounter +1)){
                    binding.btnNextStation.setText("Speichern")
                }

                     stationCounter++
                binding.etStatinNo.setText("Station $stationCounter von $amountOfStations")
            }

        }
    }
    fun saveInputAndClearFields(jsob : JSONObject){
        val jsonArr = JSONArray()
        binding.linearLayoutTargets.forEach {

            if (it is EditText) {
                jsonArr.put(it.text.toString())
                it.setText(null)

            }

        }

        var key = "st${stationCounter.toString()}"
        jsob.put(key, jsonArr)
    }
/*
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.create_parcours_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_item_save -> sendJsonToServer(jsonParcours)
        }
        return true
    }
    */

    //dummy code for sending data to Server
    fun sendJsonToServer(jsonObj : JSONObject) {

    }


}