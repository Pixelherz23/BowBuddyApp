package com.example.bowbuddyapp.ui.game

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bowbuddyapp.data.User
import com.example.bowbuddyapp.databinding.ItemPlayerBinding
import io.getstream.avatarview.coil.loadImage
import java.math.RoundingMode
import java.text.DecimalFormat

class PlayerAdapter(): RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>() {

    inner class PlayerViewHolder(val binding: ItemPlayerBinding): RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object: DiffUtil.ItemCallback<User>(){
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
            oldItem.email == newItem.email

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
            oldItem == newItem
    }
    private val differ = AsyncListDiffer(this, diffCallback)
    var players: List<User>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val binding = ItemPlayerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlayerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val player = players[position]
        holder.binding.apply {
            tvName.text = player.name
            avatarView.loadImage(player.profilImage)
            tvPoints.text = "0P"
        }
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int, payloads: MutableList<Any>) {
        if(!payloads.isEmpty()){
            if(payloads.get(0) is Int){
                val points = (payloads[0] as Int).toString() + "P"
                holder.binding.tvPoints.text = points
            }
        }else{
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemCount() = players.size
}