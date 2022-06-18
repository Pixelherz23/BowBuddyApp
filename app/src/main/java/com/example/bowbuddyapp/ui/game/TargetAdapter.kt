package com.example.bowbuddyapp.ui.game

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bowbuddyapp.data.Parcours
import com.example.bowbuddyapp.data.Points
import com.example.bowbuddyapp.databinding.ItemTargetCardBinding
import com.example.bowbuddyapp.data.Target
import com.example.bowbuddyapp.databinding.CustomAlertdialogPointsBinding
import com.example.bowbuddyapp.ui.main.ParcoursAdapter
import com.example.bowbuddyapp.viewModel.StationViewModel

class TargetAdapter(val viewModel: StationViewModel): RecyclerView.Adapter<TargetAdapter.TargetViewHolder>(){

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
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TargetViewHolder {
        val binding = ItemTargetCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TargetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TargetViewHolder, position: Int) {
        val target = targets[position]
        val context = holder.itemView.context
        val dialogBinding = CustomAlertdialogPointsBinding.inflate(LayoutInflater.from(context))

        val pointsDialog = AlertDialog.Builder(context)
            .setTitle(target.name)
            .setPositiveButton("Speichern") { _, _ ->

            }
            .setNegativeButton("Abbrechen") { _, _ ->
            }
            .setView(dialogBinding.root)
            .create()

        holder.binding.apply {
            tvTargetName.text = target.name
            tvPoints.text = "0"
            cardViewTarget.setOnClickListener {
                pointsDialog.show()
            }

        }
    }

    override fun onBindViewHolder(holder: TargetViewHolder, position: Int, payloads: MutableList<Any>) {
        if(!payloads.isEmpty()){
            if(payloads.get(0) is Int){
                holder.binding.tvPoints.text = (payloads[0] as Int).toString()
            }
        }else{
            super.onBindViewHolder(holder, position, payloads)
        }


    }



    override fun getItemCount() = targets.size
}