package com.example.bowbuddyapp.ui.game

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bowbuddyapp.data.Parcours
import com.example.bowbuddyapp.databinding.ItemTargetCardBinding
import com.example.bowbuddyapp.data.Target
import com.example.bowbuddyapp.ui.main.ParcoursAdapter

class TargetAdapter(): RecyclerView.Adapter<TargetAdapter.TargetViewHolder>(){

    inner class TargetViewHolder(val binding: ItemTargetCardBinding): RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object: DiffUtil.ItemCallback<Target>(){
        override fun areItemsTheSame(oldItem: Target, newItem: Target): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Target, newItem: Target): Boolean =
            oldItem == newItem
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var targets: List<Target>
        get() = differ.currentList
        set(value) {differ.submitList(value)}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TargetViewHolder {
        val binding = ItemTargetCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TargetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TargetViewHolder, position: Int) {
        val target = targets[position]
        val context = holder.itemView.context

        holder.binding.apply {
            this.tvTargetName.text = target.name
        }
    }

    override fun getItemCount() = targets.size
}