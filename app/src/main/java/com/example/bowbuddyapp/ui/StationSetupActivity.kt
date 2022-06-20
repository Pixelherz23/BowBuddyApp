package com.example.bowbuddyapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.core.view.size
import com.example.bowbuddyapp.R
import com.example.bowbuddyapp.databinding.ActivityStationSetupBinding
import com.example.bowbuddyapp.ui.main.MainActivity
import com.example.bowbuddyapp.viewModel.StationSetupViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject


//Todo
// fix bug at addOnChangeListener. Inserts acting weird [DONE]
// send targets stations and Parcoursinfos to API


/**
 * class for inflating the page to create stations which can have up to 5 target
 *
 *@author Kai-U. Stieler
 */
@AndroidEntryPoint
class StationSetupActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStationSetupBinding
    private val viewModel: StationSetupViewModel by viewModels()

    var amountOfStations = 0
    var stationCounter = 1
    var tempSliderCounter = 1
    lateinit var jsonParcours: JSONObject


    /**
     * Inflating the layout and adding functionality to the design elements
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStationSetupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        amountOfStations = intent.getIntExtra("amountOfStations", 1)
        binding.etStatinNo.setText("Station $stationCounter von $amountOfStations")
        jsonParcours = JSONObject(intent.getStringExtra("json"))

            var a = EditText(applicationContext)
            a.hint = "Zielbezeichnung"
            binding.linearLayoutTargets.addView(a)

        if(amountOfStations == stationCounter){
            binding.btnNextStation.setText("Speichern")

        }


        /**
         * adding functionality to Slider
         * TextViews will be created dynamically depending on the position of the slider
         */
        binding.slider.addOnChangeListener { slider, value, fromUser ->
            //Log.i("Slider Val", value.toString())
            var sliderVal = value.toInt()
            var from = slider.valueFrom.toInt()
            var to = slider.valueTo.toInt()

            if (tempSliderCounter < sliderVal) {

                var additionalViews = sliderVal - tempSliderCounter

                for (tempVal in 1..additionalViews) {
                    var temp = EditText(applicationContext)
                    temp.hint = "Zielbezeichnung"
                    binding.linearLayoutTargets.addView(temp)
                }

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

        /**
         * checks if all fields are filled. If so, it will send the station plus its targets to the server
         */
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

                if (stationCounter <= amountOfStations) {

                        binding.linearLayoutTargets.forEach {

                        if (it is EditText) {


                            var tempTarget = com.example.bowbuddyapp.data.Target(
                                0,
                                it.text.toString(),
                                "")
                            Log.i("Target", tempTarget.toString())
                            viewModel.addTarget(tempTarget)

                        }
                    }
                    Log.i("stationCounter", stationCounter.toString())
                    viewModel.setStationName("Station ${stationCounter}")

                    var x =   intent.getStringExtra("createdParcoursId")
                    Log.i("StationSetupActivity", x.toString())

                    GlobalScope.launch(Dispatchers.IO) {
                       var job = viewModel.sendStationToServer(x!!)
                        job.join()
                        viewModel.sendEachTargetToServer()
                    }


                }
                if(stationCounter == amountOfStations) {
                    //saveInputAndClearFields(jsonParcours)
                    //sendJsonToServer(jsonParcours)
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

    fun clearFields(){
        binding.linearLayoutTargets.forEach {

            if (it is EditText) {
                it.setText(null)
            }
        }
    }
}