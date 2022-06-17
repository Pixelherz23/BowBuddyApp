package com.example.bowbuddyapp.viewModel

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bowbuddyapp.data.Parcours
import com.example.bowbuddyapp.data.Statistics
import com.example.bowbuddyapp.databinding.CustomAlertdialogBinding
import com.example.bowbuddyapp.databinding.ItemHitProbabilityCardBinding
import com.example.bowbuddyapp.databinding.ItemParcoursCardBinding
import com.example.bowbuddyapp.databinding.ItemPlacementCardBinding
import com.example.bowbuddyapp.ui.game.PreGame
import com.example.bowbuddyapp.ui.main.ParcoursAdapter
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter

class StatisticsAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var counter = 0
    lateinit var statistics: Statistics


    //holds the views of the items that are currently displayed
    inner class StatisticsViewHolderProbability(val binding: ItemHitProbabilityCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class StatisticsViewHolderPlacement(val binding: ItemPlacementCardBinding) :
        RecyclerView.ViewHolder(binding.root)




    //called when the recycler needs a new view holder (for wexample when scrolling)
    //viewGroup: collection of views. (LinearLayout for example)
    //parent = the recylerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        //"inflates" the item_card.xml file, so we can access it with kotlin
        // val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent,false)
        Log.i("counter: ", counter.toString())
        if (counter == 0) {
            counter++
            return StatisticsViewHolderProbability(
                ItemHitProbabilityCardBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            counter++
            return StatisticsViewHolderPlacement(
                ItemPlacementCardBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )

        }




        //val binding = ItemHitProbabilityCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        // return StatisticsViewHolderProbability(binding)
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }
    //binding the data to our views/items
    //adding functionality

    /*
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is StatisticsViewHolderProbability) {
            holder.binding.tvPointsPercentage.text = "${elem.hitProbability}%"

        } else if (holder is StatisticsViewHolderPlacement){
           var totalAmount = elem.placementFirst + elem.placementSecond + elem.placementThird
            holder.binding.totalAmount.text = "$totalAmount Platzierungen"




            val list = mutableListOf<PieEntry>()
            list.add(PieEntry(percentage(elem.placementFirst, totalAmount),"1. Platz"))
            list.add(PieEntry(percentage(elem.placementSecond, totalAmount),"2. Platz"))
            list.add(PieEntry(percentage(elem.placementThird, totalAmount),"3. Platz"))

            var dataSet = PieDataSet(list,"")
            dataSet.setColors(getColors())

            var data = PieData(dataSet)
            data.apply {
                setDrawValues(true)
                setValueFormatter(PercentFormatter(holder.binding.activityMainPiechart))
                setValueTextSize(12f)
                setValueTextColor(Color.BLACK)
            }

            holder.binding.activityMainPiechart.apply{
                description.isEnabled = false
                setDrawEntryLabels(false)
                setUsePercentValues(true)
                setData(data)
                invalidate()
            }
    }
*/


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is StatisticsViewHolderProbability) {
            holder.binding.tvPointsPercentage.text = "${statistics.hitProbability}%"

        } else if (holder is StatisticsViewHolderPlacement){
            var totalAmount = statistics.placementFirst + statistics.placementSecond + statistics.placementThird
            holder.binding.totalAmount.text = "$totalAmount Platzierungen"




            val list = mutableListOf<PieEntry>()
            list.add(PieEntry(percentage(statistics.placementFirst, totalAmount),"1. Platz"))
            list.add(PieEntry(percentage(statistics.placementSecond, totalAmount),"2. Platz"))
            list.add(PieEntry(percentage(statistics.placementThird, totalAmount),"3. Platz"))

            var dataSet = PieDataSet(list,"")
            dataSet.setColors(getColors())

            var data = PieData(dataSet)
            data.apply {
                setDrawValues(true)
                setValueFormatter(PercentFormatter(holder.binding.activityMainPiechart))
                setValueTextSize(12f)
                setValueTextColor(Color.BLACK)
            }

            holder.binding.activityMainPiechart.apply{
                description.isEnabled = false
                setDrawEntryLabels(false)
                setUsePercentValues(true)
                setData(data)
                invalidate()
            }
        }




}
    fun getColors(): MutableList<Int> {
        val colors = mutableListOf<Int>()
        colors.add(Color.rgb(255,223,0))
        colors.add(Color.rgb(192,192,192))
        colors.add(Color.rgb(191,137,112))

        return colors
    }


    fun percentage( count: Int, total: Int ):Float{

        return (count.toFloat() / total.toFloat())
    }

override fun getItemCount(): Int {
        return 2
    }
}