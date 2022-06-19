package com.example.bowbuddyapp.ui.game

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.bowbuddyapp.data.Station

class StationAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {

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

    lateinit var link: String
    lateinit var rule: String

    override fun createFragment(position: Int) = StationFragment.newInstance(stations[position].id, stations[position].name, link, rule)

    override fun getItemCount() = stations.size

}