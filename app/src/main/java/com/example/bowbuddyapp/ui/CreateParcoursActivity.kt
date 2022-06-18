package com.example.bowbuddyapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleObserver
import com.example.bowbuddyapp.data.Parcours
import com.example.bowbuddyapp.databinding.ActivityCreateParcoursBinding
import com.example.bowbuddyapp.viewModel.CreateParcoursViewModel
import com.example.bowbuddyapp.viewModel.ParcoursViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import javax.inject.Inject


/*
sendParcoursToHomeFragment : take user unfo and acutally send it

TestCase:
What happens if internet connection gets lost?
 */


@AndroidEntryPoint
class CreateParcoursActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateParcoursBinding

    private val viewModel: CreateParcoursViewModel by viewModels()

    @Inject
    lateinit var mGoogleSignInAcc : GoogleSignInAccount

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
                mGoogleSignInAcc.email.toString()
            )
            Log.i("Parcours: ", parcours.toString())

            viewModel.parcours.value = parcours
            var job = viewModel.sendParcours()

            //This will crash the app


            viewModel.parcoursId.observe(this){ parcoursId ->
                val intent = Intent(applicationContext, StationSetupActivity::class.java)
                intent.putExtra("amountOfStations",binding.slider.value.toInt())
                intent.putExtra("json",parcoursDataToJson().toString() )
                Log.i("parcoursId from CreateParcours: ", parcoursId.toString())
                intent.putExtra("createdParcoursId",parcoursId.toString())
                startActivity(intent)
            }









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