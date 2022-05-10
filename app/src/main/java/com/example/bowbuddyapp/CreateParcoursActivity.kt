package com.example.bowbuddyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_create_parcours.*
import kotlinx.coroutines.*
import okhttp3.RequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit



/*
sendParcoursToHomeFragment : take user unfo and acutally send it

TestCase:
What happens if internet connection gets lost?
 */
class CreateParcoursActivity : AppCompatActivity() {
    val BASE_URL = "https://dummy.restapiexample.com"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_parcours)

        btn_next.setOnClickListener {
            val intent = Intent(applicationContext, StationSetupActivity::class.java)
            intent.putExtra("amountOfStations",slider.value.toInt())
            intent.putExtra("json",parcoursDataToJson().toString() )
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.create_parcours_bar_menu, menu)
        return true
    }


//dummy code for sending data to Server
    /*
    fun sendParcoursToServer() {
        Log.i("Click", "Function call")
        // Create Retrofit
        val retrofit = Retrofit.Builder()
            //.addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
        val service = retrofit.create(BowBuddyAPI::class.java)

        // Create JSON using JSONObject (dummy code to see if it works)
        val jsonObject = JSONObject()
        jsonObject.put("name", "Jack")
        jsonObject.put("salary", "3540")
        jsonObject.put("age", "23")


        val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())

        //What is the difference between GlobalScope(Dispatchers.Main).launch and line 52
        CoroutineScope(Dispatchers.Main).launch {
            //fix for inappropriate blocking method call" for response.body()?.string().toString()
            kotlin.runCatching {
                // Do the POST request and get response
                val response = service.createEmployee(requestBody )

            if (response.isSuccessful) {

                val jsonAsString = response.body()?.string().toString()
                val result = JsonParser().parse(jsonAsString).asJsonObject
                //Log.i("RETROFIT_NICE_jsonAsString", jsonAsString)
                Log.i("RETROFIT_NICE:", result.get("status").toString())
                Toast.makeText(applicationContext, "Parcours erstellt ", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                val errCode = response.code().toString()

                Log.e("POST_Parcours_ERROR", errCode)
                Toast.makeText(applicationContext, "Etwas ist falsch gelaufen: $errCode", Toast.LENGTH_SHORT).show()
            }
            }
        }
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

    jsonParcours.put("parcoursName", editText_parcoursname.text.toString())
    jsonParcours.put("price", editText_price.text.toString())
    jsonParcours.put("street", editText_street.text.toString())
    jsonParcours.put("city", editText_city.text.toString())
    jsonParcours.put("inf", editText_info.text.toString())
    jsonParcours.put("amountOfStations",slider.value.toString())

    return jsonParcours



}



}