package com.example.bowbuddyapp.game

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.core.view.forEach
import androidx.recyclerview.widget.RecyclerView
import com.example.bowbuddyapp.R
import kotlinx.android.synthetic.main.custom_alertdialog_points.view.*
import kotlinx.android.synthetic.main.item_target_card.view.*
import org.json.JSONArray
import org.json.JSONObject

class GameAdapter (var elem: List<Target>, var context: Context, var gameRule : String) : RecyclerView.Adapter<GameAdapter.AdapterHodl>() {
    lateinit var view: View
    //numpicker 0,1,2,3,4
    val dsb = arrayOf("11","10","8","5","0")

    val firstArrow = arrayOf("20","18","0")
    val secondArrow = arrayOf("16","14","0")
    val thirdArrow = arrayOf("12","10","0")
    val dfbv = arrayOf(firstArrow, secondArrow, thirdArrow)





    //holds the views of the items that are currently displayed
    inner class AdapterHodl(itemView: View) : RecyclerView.ViewHolder(itemView)

    //called when the recycler needs a new view holder (for wexample when scrolling)
    //viewGroup: collection of views. (LinearLayout for example)
    //parent = the recylerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterHodl {
        //"inflates" the item_card.xml file, so we can access it with kotlin
        // val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent,false)
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_target_card, parent, false)

        return AdapterHodl(view)
    }

    //binding the data (elem) to our views/items
    //adding functionality
    override fun onBindViewHolder(holder: AdapterHodl, position: Int) {

        holder.itemView.tv_target_name.text = elem[position].name

        holder.itemView.card_view_target.setOnClickListener {
            val builder = AlertDialog.Builder(context)
                .setTitle(elem[position].name)
                .setPositiveButton("speichern") { _, _ ->

                    var i = 0
                    view.linearLayout_alertDialog_points.forEach {

                        var tempMutableList = mutableListOf<Int>()
                        if (it is NumberPicker) {
                            elem[position].pointsPerArrow[i]= it.value

                            i++

                        }
                        var tempPoints = 0
                        for(x in convertSpinnerData( elem[position].pointsPerArrow, gameRule )){
                            tempPoints += x
                        }
                        elem[position].points = tempPoints
                        holder.itemView.tv_points.text = elem[position].points.toString()

                    }
                }
                .create()



            view = LayoutInflater.from(context).inflate(R.layout.custom_alertdialog_points,null)
            //view.numberPicker_one.val

            /*
            var testArr = Array<String>(3)
            testArr[0] = "3"
            testArr[0] = "8"
            testArr[0] = "16"
            */
            var i = 0
            view.linearLayout_alertDialog_points.forEach {

                if (it is NumberPicker) {

                    if(gameRule.equals("DSB (WA)")){
                        it.minValue = 0
                        it.maxValue = dsb.size-1
                        it.displayedValues = dsb
                        it.value = elem[position].pointsPerArrow[i]
                        i++
                    }else if(gameRule.equals("DFBV (IFAA)")){
                        it.minValue = 0
                        it.maxValue = dfbv[i].size-1
                        it.displayedValues = dfbv[i]
                        it.value = elem[position].pointsPerArrow[i]
                        i++
                    }



                    it.wrapSelectorWheel = false
                    it.setOnValueChangedListener{ numberPicker, oldVal, newVal ->
                        Log.i("Numpicker","$newVal")

                    }
                }

            }
            /*
            view.lvl1.numberPicker_one.minValue = 0
            view.lvl1.numberPicker_one.maxValue = dsb.size-1
            view.lvl1.numberPicker_one.displayedValues = dsb


            view.lvl1.numberPicker_one.wrapSelectorWheel = false
            view.lvl1.numberPicker_one.setOnValueChangedListener{ numberPicker, oldVal, newVal ->
                Log.i("Numpicker","$newVal")

            }
            */
            //linearLayout_alertDialog_points
            builder.setView(view)
            builder.show()
        }



    }

    fun convertSpinnerData(spinnerPoints: IntArray, gameRule: String ): IntArray{
        var intArr = IntArray(3)

        if(gameRule.equals("DSB (WA)")){
           //val dsb = arrayOf("11","10","8","5","0")
            // 11 = 0, 10 = 1, 8 = 2

                var i = 0
            for(x in spinnerPoints){
                if(x == 0){
                    intArr[i] = 11
                }
                if(x == 1){
                    intArr[i] = 10
                }
                if(x == 2){
                    intArr[i] = 8
                }
                if(x == 3){
                    intArr[i] = 5
                }
                if(x == 4){
                    intArr[i] = 0
                }

                i++
            }
        }else if(gameRule.equals("DFBV (IFAA)")){

            if(spinnerPoints[0] == 0){
                intArr[0] = 20
            }else if(spinnerPoints[0] == 1){
                intArr[0] = 18
            }else{
                intArr[0] = 0
            }

            if(spinnerPoints[1] == 0){
                intArr[1] = 16
            }else if(spinnerPoints[1] == 1){
                intArr[1] = 14
            }else{
                intArr[1] = 0
            }

            if(spinnerPoints[2] == 0){
                intArr[2] = 12
            }else if(spinnerPoints[2] == 1){
                intArr[2] = 10
            }else{
                intArr[2] = 0
            }


        }

        return intArr
    }



    override fun getItemCount(): Int {
        return elem.size
    }
}