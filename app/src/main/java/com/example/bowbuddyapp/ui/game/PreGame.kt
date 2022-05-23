package com.example.bowbuddyapp.ui.game

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.example.bowbuddyapp.R
import com.example.bowbuddyapp.databinding.ActivityPreGameBinding
import com.example.bowbuddyapp.viewModel.PreGameViewModel


class PreGame : AppCompatActivity() {

    private val viewModel: PreGameViewModel by viewModels()

    private lateinit var binding: ActivityPreGameBinding
    lateinit var gameRule : String
    var itemFlag = false
    var multiplayerFlag = false
    val STATIC_DUMMY_LINK = "https://bowbuddy.de/STATICLINK"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //anonymous class
        binding.apply {
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(adapter: AdapterView<*>?, view: View?, indexOfItem: Int, idOfItem: Long) {
                    gameRule = adapter?.getItemAtPosition(indexOfItem).toString()
                    Log.i("Item", gameRule)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

            }
            swMultiplayer.setOnCheckedChangeListener { _, isChecked ->
                multiplayerFlag = isChecked
                if(isChecked == true && itemFlag == false){
                    var imageBtn = ImageButton(applicationContext)
                    imageBtn.setImageDrawable(ResourcesCompat.getDrawable(resources,
                        R.drawable.ic_share,null ))
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

                    binding.linearLayoutHorizontal.apply {
                        addView(temp)
                        addView(invideLink)
                        addView(imageBtn)
                        addView(invideLink)
                        addView(imageBtn)
                    }

                    itemFlag = true
                }else if(isChecked == false && itemFlag == true){
                    binding.apply {
                        linearLayoutPregameMultiplayer.removeView(findViewById(R.id.tv_capital))
                        linearLayoutHorizontal.removeAllViewsInLayout()
                    }

                    itemFlag = false
                }

                val message = if (isChecked) "Switch1:ON" else "Switch1:OFF"
                Log.i("MSG", message)
            }

            btnStart.setOnClickListener {

                //TODO change null with next activity
                // probably pass the parcoursid, so the next activity can make a request to get the Stations etc


                val intent = Intent(applicationContext,  GameActivity::class.java)
                intent.putExtra("isMultiplayer",multiplayerFlag)
                intent.putExtra("gameRule", gameRule)
                startActivity(intent)

            }

        }






    }
//TODO This is a dummy method. replace with api call


    
}

