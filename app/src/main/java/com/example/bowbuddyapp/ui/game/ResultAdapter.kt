package com.example.bowbuddyapp.ui.game

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bowbuddyapp.data.User
import com.example.bowbuddyapp.databinding.ItemResultBinding
import io.getstream.avatarview.coil.loadImage

class ResultAdapter(rule: String, targets: Int, points: Int): RecyclerView.Adapter<ResultAdapter.ResultViewHolder>() {

    inner class ResultViewHolder(val binding: ItemResultBinding): RecyclerView.ViewHolder(binding.root)

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val binding = ItemResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val player = players[position]
        val maxPoints = 0

        holder.binding.apply {
            tvName.text = player.name
            avatarView.loadImage(player.profilImage)
        }

    }

    override fun onBindViewHolder(holder: ResultAdapter.ResultViewHolder, position: Int, payloads: MutableList<Any>) {
        if(!payloads.isEmpty()){
            if(payloads.get(0) is Int){
                val hits = (payloads[0] as Int).toString() + "/"
                holder.binding.tvHitsValue.text = hits
            }
        }else{
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemCount() = players.size


}
