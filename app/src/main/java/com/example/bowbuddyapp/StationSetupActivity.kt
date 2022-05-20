package com.example.bowbuddyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.view.forEach
import androidx.core.view.size
import com.google.android.material.slider.Slider
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_create_parcours.*
import kotlinx.android.synthetic.main.activity_station_setup.*
import kotlinx.android.synthetic.main.activity_station_setup.slider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit


//Todo
// fix bug at addOnChangeListener. Inserts acting weird [DONE]
// send targets stations and Parcoursinfos to API

class StationSetupActivity : AppCompatActivity() {
    //val BASE_URL = "https://dummy.restapiexample.com"
    val BASE_URL = "https://59baf216-74eb-4959-9c0c-f38ed4849c5b.mock.pstmn.io"

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
        setContentView(R.layout.activity_station_setup)
        amountOfStations = intent.getIntExtra("amountOfStations", 1)
        et_statinNo.setText("Station $stationCounter von $amountOfStations")
        jsonParcours = JSONObject(intent.getStringExtra("json"))

            var a = EditText(applicationContext)
            a.hint = "Zielbezeichnung"
            //a.id = idList[0]
            linearLayout_Targets.addView(a)

        if(amountOfStations == stationCounter){
            btn_nextStation.setText("Speichern")

        }



        //If you only want the slider start and end value and don't care about the previous values
        slider.addOnChangeListener { slider, value, fromUser ->
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
                    linearLayout_Targets.addView(temp)

                }
                //linearLayout_Targets.addView(EditText(applicationContext))

                tempSliderCounter = sliderVal

            } else if (tempSliderCounter >= sliderVal) {
                var removeViews = tempSliderCounter - sliderVal
                var x = linearLayout_Targets.size - 1
                for (tempVal in 1..removeViews) {

                    linearLayout_Targets.removeViewAt(x)
                    x--
                }

                tempSliderCounter = sliderVal
            }

        }

        btn_nextStation.setOnClickListener {
            var flag = false

            linearLayout_Targets.forEach {

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
                    btn_nextStation.setText("Speichern")
                }

                     stationCounter++
                et_statinNo.setText("Station $stationCounter von $amountOfStations")
            }

        }
    }
    fun saveInputAndClearFields(jsob : JSONObject){
        val jsonArr = JSONArray()
        linearLayout_Targets.forEach {

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
        Log.i("Json:", jsonObj.toString(2))
        // Create Retrofit
        val retrofit = Retrofit.Builder()
            //.addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
        val service = retrofit.create(BowBuddyAPI::class.java)

        val requestBody = jsonObj.toString().toRequestBody("application/json".toMediaTypeOrNull())

        //What is the difference between GlobalScope(Dispatchers.Main).launch and line 52
        CoroutineScope(Dispatchers.Main).launch {
            //fix for "inappropriate blocking method call" for response.body()?.string().toString()
            kotlin.runCatching {
                // Do the POST request and get response
                val response = service.createParcours(requestBody)

                if (response.isSuccessful) {

                    val jsonAsString = response.body()?.string().toString()
                    val result = JsonParser().parse(jsonAsString).asJsonObject
                    //Log.i("RETROFIT_NICE_jsonAsString", jsonAsString)
                    Log.i("RETROFIT_NICE:", result.get("status").toString())
                    Toast.makeText(applicationContext, "Parcours erstellt ", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                } else {
                    val errCode = response.code().toString()

                    Log.e("POST_Parcours_ERROR", errCode)
                    Toast.makeText(
                        applicationContext,
                        "Etwas ist falsch gelaufen: $errCode",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


}