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
import com.example.bowbuddyapp.viewModel.CreateParcoursViewModel
import com.example.bowbuddyapp.viewModel.SinglePlayerViewModel
import com.example.bowbuddyapp.viewModel.StationSetupViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject


//Todo
// fix bug at addOnChangeListener. Inserts acting weird [DONE]
// send targets stations and Parcoursinfos to API

@AndroidEntryPoint
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

    private val viewModel: StationSetupViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStationSetupBinding.inflate(layoutInflater)
        setContentView(binding.root)

       // Log.i("testModel", testModel.stations.value.toString())
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


                           //viewModel.stationName.value = stationCounter.toString()


                    }
                    Log.i("stationCounter", stationCounter.toString())
                    viewModel.setStationName(stationCounter.toString())

                    var x =   intent.getStringExtra("createdParcoursId")
                    Log.i("StationSetupActivity", x.toString())

                    /*
                runBlocking {
                    var y =  viewModel.sendStationToServer(x!!)
                    y.join()
                    viewModel.sendEachTargetToServer()
                }
                */

                    GlobalScope.launch(Dispatchers.IO) {
                       var job = viewModel.sendStationToServer(x!!)
                        job.join()
                        viewModel.sendEachTargetToServer()
                    }

                    /*
                    viewModel.sendStationToServer(x!!)
                    viewModel._stationID.observe(this) { _stationID ->
                        Log.i("SSA", "stationID  $_stationID changed, sending Target")
                        viewModel.sendEachTargetToServer()
                    }
                    //viewModel.clear()
                    clearFields()
                    //viewModel.removeAllTargets()
                     */


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