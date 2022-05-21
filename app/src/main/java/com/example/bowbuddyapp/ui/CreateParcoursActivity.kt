package com.example.bowbuddyapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bowbuddyapp.databinding.ActivityCreateParcoursBinding
import org.json.JSONObject


/*
sendParcoursToHomeFragment : take user unfo and acutally send it

TestCase:
What happens if internet connection gets lost?
 */



class CreateParcoursActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateParcoursBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateParcoursBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNext.setOnClickListener {
            val intent = Intent(applicationContext, StationSetupActivity::class.java)
            intent.putExtra("amountOfStations",binding.slider.value.toInt())
            intent.putExtra("json",parcoursDataToJson().toString() )
            startActivity(intent)
        }
    }
    /*
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.create_parcours_bar_menu, menu)
        return true
    }
    */



    /*
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_item_save -> sendParcoursToServer()
        }
        return true
    }
*/

fun parcoursDataToJson(): JSONObject {
    val jsonParcours = JSONObject()
    return jsonParcours
}



}