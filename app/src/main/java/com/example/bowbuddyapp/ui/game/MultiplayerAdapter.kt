package com.example.bowbuddyapp.ui.game

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bowbuddyapp.data.Station
import com.example.bowbuddyapp.databinding.ItemMultiplayerBinding

class MultiplayerAdapter() : RecyclerView.Adapter<MultiplayerAdapter.ViewPagerViewHolder>(){

    inner class ViewPagerViewHolder(val binding: ItemMultiplayerBinding): RecyclerView.ViewHolder(binding.root)

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val binding = ItemMultiplayerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewPagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val station = stations[position]
        val context = holder.itemView.context

    }

    override fun getItemCount() = stations.size

}