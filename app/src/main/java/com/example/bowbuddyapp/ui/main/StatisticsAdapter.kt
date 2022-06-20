package com.example.bowbuddyapp.ui.main

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bowbuddyapp.data.Statistics
import com.example.bowbuddyapp.databinding.ItemHitProbabilityCardBinding
import com.example.bowbuddyapp.databinding.ItemPlacementCardBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter


/**
 * Adapter for loading the data of [statistics] in item card
 * The adapter is necessary for the recyclerview
 *
 * @author Kai-U. Stieler, Lukas Beckmann (co-author)
 */
class StatisticsAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var counter = 0
    lateinit var statistics: Statistics


    //holds the views of the items that are currently displayed
    inner class StatisticsViewHolderProbability(val binding: ItemHitProbabilityCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class StatisticsViewHolderPlacement(val binding: ItemPlacementCardBinding) :
        RecyclerView.ViewHolder(binding.root)




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
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
    }



    /**
     * binding the data to to the ProbabilityCard and to the PiechartCard
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

    /**
     * Color settings for piechart
     */
    fun getColors(): MutableList<Int> {
        val colors = mutableListOf<Int>()
        colors.add(Color.rgb(255,223,0))
        colors.add(Color.rgb(192,192,192))
        colors.add(Color.rgb(191,137,112))

        return colors
    }


    /**
     * calculating percentage
     */
    fun percentage( count: Int, total: Int ):Float{
        return (count.toFloat() / total.toFloat())
    }

    /**
     * In this case, we returning how many itemcards we want.
     */
    override fun getItemCount(): Int {
        return 2
    }
}