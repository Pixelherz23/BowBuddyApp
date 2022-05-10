package com.example.bowbuddyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.annotation.NonNull
import androidx.core.view.forEach
import androidx.core.view.size
import com.google.android.material.slider.Slider
import kotlinx.android.synthetic.main.activity_create_parcours.*
import kotlinx.android.synthetic.main.activity_station_setup.*
import kotlinx.android.synthetic.main.activity_station_setup.slider
import org.json.JSONObject


//Todo
// fix bug at addOnChangeListener. Inserts acting weird [DONE]
// send targets stations and Parcoursinfos to API

class StationSetupActivity : AppCompatActivity() {
    var amountOfStations = 0
    var stationCounter = 1
    var tempSliderCounter = 1
    lateinit var jsonParcours : JSONObject

    val idList = mutableListOf(R.id.et_target1,R.id.et_target2,R.id.et_target3,R.id.et_target4,R.id.et_target5)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_station_setup)
        amountOfStations = intent.getIntExtra("amountOfStations", 1)
        et_statinNo.setText("Station $stationCounter von $amountOfStations")
        jsonParcours = JSONObject(intent.getStringExtra("json"))

        if(slider.value.toInt() == 1){
           var a = EditText(applicationContext)
            a.hint = "Zielbezeichnung"
            //a.id = idList[0]
            linearLayout_Targets.addView(a)

        }


        //If you only want the slider start and end value and don't care about the previous values
        slider.addOnChangeListener { slider, value, fromUser ->
            //val values = slider.value.toString()
                Log.i("Slider Val", value.toString())
            var sliderVal = value.toInt()
            var from = slider.valueFrom.toInt()
            var to = slider.valueTo.toInt()
            /*
            for (tempVal in from..to) {
                linearLayout_Targets.addView(EditText(applicationContext))
            }
*/
            if(tempSliderCounter < sliderVal){

                var additionalViews = sliderVal - tempSliderCounter

                for (tempVal in 1..additionalViews) {
                    var temp = EditText(applicationContext)
                    temp.hint = "Zielbezeichnung"
                    //temp.id = idList[0]
                    linearLayout_Targets.addView(temp)

                }

                //linearLayout_Targets.addView(EditText(applicationContext))

                tempSliderCounter = sliderVal

            }else if(tempSliderCounter >= sliderVal){
                var removeViews = tempSliderCounter - sliderVal
                var x = linearLayout_Targets.size -1
                for (tempVal in 1..removeViews) {

                    linearLayout_Targets.removeViewAt(x)
                    x--
                }

                tempSliderCounter = sliderVal
            }

    }
        //Todo work in progress
        btn_nextStation.setOnClickListener {
            if(stationCounter < amountOfStations ){
                val arrTargetName = mutableListOf<String>()
                linearLayout_Targets.forEach {

                    if(it is EditText){
                        arrTargetName.add(it.text.toString())
                        it.setText("")

                    }

                }
                jsonParcours.put(stationCounter.toString(), arrTargetName)
            }else{
                //send it
            }
            stationCounter++
        }




}
}