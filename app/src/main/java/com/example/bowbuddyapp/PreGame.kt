package com.example.bowbuddyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import kotlinx.android.synthetic.main.activity_create_parcours.*
import kotlinx.android.synthetic.main.activity_pre_game.*
import kotlinx.android.synthetic.main.activity_station_setup.*
import kotlinx.android.synthetic.main.activity_station_setup.slider

class PreGame : AppCompatActivity() {
    lateinit var gameRule : String
    var itemFlag = false
    var multiplayerFlag = false
    val STATIC_DUMMY_LINK = "https://bowbuddy.de/STATICLINK"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_game)

        //anonymous class
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapter: AdapterView<*>?, view: View?, indexOfItem: Int, idOfItem: Long) {
                gameRule = adapter?.getItemAtPosition(indexOfItem).toString()
                Log.i("Item", gameRule)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        sw_multiplayer.setOnCheckedChangeListener { _, isChecked ->
            multiplayerFlag = isChecked
            if(isChecked == true && itemFlag == false){
                var imageBtn = ImageButton(applicationContext)
                imageBtn.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_share,null ))
                imageBtn.setOnClickListener{
                    var intent = Intent(Intent.ACTION_SEND)

                    intent.putExtra(Intent.EXTRA_TEXT, STATIC_DUMMY_LINK)
                    intent.type = "text/plain"
                    startActivity(intent)
                }
                var temp = TextView(applicationContext)
                temp.id = (R.id.tv_capital)


                temp.setText("Einladungslink")
                //temp.setCompoundDrawablesWithIntrinsicBounds(0, 0,, 0);
                var invideLink = EditText(applicationContext)
                invideLink.setText(STATIC_DUMMY_LINK)
                invideLink.isFocusable = false


                linearLayout_pregame_multiplayer.addView(temp)

                linearLayout_horizontal.addView(invideLink)
                linearLayout_horizontal.addView(imageBtn)


                itemFlag = true
            }else if(isChecked == false && itemFlag == true){
                linearLayout_pregame_multiplayer.removeView(findViewById(R.id.tv_capital))
                    linearLayout_horizontal.removeAllViewsInLayout()
                itemFlag = false
            }

            val message = if (isChecked) "Switch1:ON" else "Switch1:OFF"
            Log.i("MSG", message)
        }

            btn_start.setOnClickListener {

                //TODO change null with next activity
                // probably pass the parcoursid, so the next activity can make a request to get the Stations etc


                val intent = Intent(applicationContext, null)
                intent.putExtra("isMultiplayer",multiplayerFlag)
                intent.putExtra("gameRule", gameRule)
                startActivity(intent)

            }
    }
}

