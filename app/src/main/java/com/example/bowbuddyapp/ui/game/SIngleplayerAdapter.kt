package com.example.bowbuddyapp.ui.game

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bowbuddyapp.data.Station
import com.example.bowbuddyapp.data.Target
import com.example.bowbuddyapp.databinding.ItemSingleplayerBinding
import dagger.hilt.android.AndroidEntryPoint

class SIngleplayerAdapter() : RecyclerView.Adapter<SIngleplayerAdapter.ViewPagerViewHolder>(){

    inner class ViewPagerViewHolder(val binding: ItemSingleplayerBinding): RecyclerView.ViewHolder(binding.root)


    private val diffCallback = object: DiffUtil.ItemCallback<Station>(){
        override fun areItemsTheSame(oldItem: Station, newItem: Station): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Station, newItem: Station): Boolean =
            oldItem == newItem
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var stations: List<Station>
        get() = differ.currentList
        set(value) {differ.submitList(value)}

    lateinit var targets: List<List<Target>>



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
     val binding = ItemSingleplayerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
     return ViewPagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val station = stations[position]
        val targets = targets[position]
        val context = holder.itemView.context
        val targetAdapter = TargetAdapter()
        Log.i("ADAPTER", station.toString() + " | " + targets.toString())

        targetAdapter.targets = targets
        holder.binding.apply {
            tvStationName.text = station.name
            tvName.text = "MaxMuster"
            tvPoints.text = "0P"
            rvSingleplayer.apply {
               layoutManager = LinearLayoutManager(context)
               adapter = targetAdapter

           }

       }

    }

    override fun getItemCount() = stations.size

}