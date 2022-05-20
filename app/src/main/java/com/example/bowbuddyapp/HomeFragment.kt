package com.example.bowbuddyapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit

//Fragment(R.layout.fragment_home) does the same as on Create View


class HomeFragment(context: Context) : Fragment() {
    val BASE_URL = "https://dummy.restapiexample.com"


    var initParcours = mutableListOf<ParcoursCard>()

    val adapter = ParcoursAdapter(initParcours, context)




    fun fillParcoursList(){

        //Following code is sample code
        Log.i("Function_Call", "fillParcoursList()")
        // Create Retrofit
        val retrofit = Retrofit.Builder()
            //.addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
        val service = retrofit.create(BowBuddyAPI::class.java)

        CoroutineScope(Dispatchers.IO).launch {

            val response = service.getEmployees()

            CoroutineScope(Dispatchers.Main).launch {
                if (response.isSuccessful) {
                    /*
                    val jsonAsString = response.body()?.string().toString()
                    val result = JsonParser().parse(jsonAsString).asJsonObject
                    Log.i("Employee_Json",result.toString())

                     */
                    //Log.d("Pretty Printed JSON :", prettyJson)

                } else {

                    Log.e("RETROFIT_ERROR", response.code().toString())

                }
            }
        }
    }

    //view are not accessable yet cause the arent inflated yet
    //in onView Created they are accessable though
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //fillParcoursList()
        view_recycler.adapter = adapter
        view_recycler.layoutManager = LinearLayoutManager(context)


        //initParcours.add(ParcoursCard("Bogenbahn1",12,"","","",2))

        btn_create_new_parcours.setOnClickListener {
            Log.i("btn", "Button for creating new parcours is clicked")
            //initParcours.add(ParcoursCard("New Parcours"))
            //adapter.notifyItemInserted(initParcours.size-1) //?? why
            val intent = Intent(context, CreateParcoursActivity::class.java)
            startActivity(intent)

        }



    }

    override fun onResume() {
        Log.i("onResume", "activated")
        super.onResume()
        fillParcoursList()
    }


}