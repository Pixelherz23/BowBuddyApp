package com.example.bowbuddyapp

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_parcours)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.create_parcours_bar_menu, menu)
        return true
    }


    fun sendParcoursToHomeFragment() {
        Log.i("Click", "Function call")
        // Create Retrofit
        val retrofit = Retrofit.Builder()
            //.addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://dummy.restapiexample.com")
            .build()
        val service = retrofit.create(BowBuddyAPI::class.java)

        // Create JSON using JSONObject (dummy code to see if it works)
        val jsonObject = JSONObject()
        jsonObject.put("name", "Jack")
        jsonObject.put("salary", "3540")
        jsonObject.put("age", "23")

        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()

        // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
        //What is the difference between GlobalScope(Dispatchers.Main).launch and line 52
        CoroutineScope(Dispatchers.Main).launch {

            //fix for inappropriate blocking method call" for response.body()?.string().toString()
            kotlin.runCatching {
                // Do the POST request and get response
                val response = service.createEmployee(requestBody )

            if (response.isSuccessful) {

                //Log.i("RETROFIT_NICE", response.body()?.string().toString())

                val jsonAsString = response.body()?.string().toString()
                val result = JsonParser().parse(jsonAsString).asJsonObject
                //Log.i("RETROFIT_NICE_jsonAsString", jsonAsString)
                Log.i("RETROFIT_NICE:", result.get("status").toString())

            } else {

                Log.e("RETROFIT_ERROR", response.code().toString())

            }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_item_save -> sendParcoursToHomeFragment()

        }

        return true
    }
}