package com.example.bowbuddyapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.example.bowbuddyapp.data.Parcours
import com.example.bowbuddyapp.databinding.ActivityCreateParcoursBinding
import com.example.bowbuddyapp.viewModel.CreateParcoursViewModel
import com.example.bowbuddyapp.viewModel.ParcoursViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject


/*
sendParcoursToHomeFragment : take user unfo and acutally send it

TestCase:
What happens if internet connection gets lost?
 */


@AndroidEntryPoint
class CreateParcoursActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateParcoursBinding

    private val viewModel: CreateParcoursViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateParcoursBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNext.setOnClickListener {


            //TODO Data class maybe should have CITY and STREET. Not only "address"
            //T
            var parcours = Parcours(
                0,
                binding.editTextParcoursname.text.toString(),
                binding.editTextCity.text.toString(),
                binding.editTextPrice.text.toString(),
                binding.editTextInfo.text.toString(),
                "test@api.com"
            )
            Log.i("Parcours: ", parcours.toString())

            viewModel.parcours.value = parcours
            viewModel.sendParcours()

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