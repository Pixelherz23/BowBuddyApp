package com.example.bowbuddyapp.ui.main


import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bowbuddyapp.data.Parcours
import com.example.bowbuddyapp.databinding.CustomAlertdialogBinding
import com.example.bowbuddyapp.databinding.ItemParcoursCardBinding
import com.example.bowbuddyapp.ui.game.PreGame

//Job of adapter: creating views for our items that have in our recycler
//short: binds data to views(item_card.xml)
class ParcoursAdapter() : RecyclerView.Adapter<ParcoursAdapter.ParcourViewHolder>() {

    //holds the views of the items that are currently displayed
    inner class ParcourViewHolder(val binding: ItemParcoursCardBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object: DiffUtil.ItemCallback<Parcours>(){
        override fun areItemsTheSame(oldItem: Parcours, newItem: Parcours): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Parcours, newItem: Parcours): Boolean =
            oldItem == newItem
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var parcours: List<Parcours>
        get() = differ.currentList
        set(value) {differ.submitList(value)}

    //called when the recycler needs a new view holder (for wexample when scrolling)
    //viewGroup: collection of views. (LinearLayout for example)
    //parent = the recylerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParcourViewHolder {
        //"inflates" the item_card.xml file, so we can access it with kotlin
        // val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent,false)

        val binding = ItemParcoursCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ParcourViewHolder(binding)
    }

    //binding the data to our views/items
    //adding functionality
    override fun onBindViewHolder(holder: ParcourViewHolder, position: Int) {
        val parcour = parcours[position]
        val context = holder.itemView.context
        val dialogBinding = CustomAlertdialogBinding.inflate(LayoutInflater.from(context))

        dialogBinding.apply {
            etStreet.text = parcour.address
            etPrice.text = parcour.price
            etInfoDescription.text = parcour.info
        }

        val shootDialog = AlertDialog.Builder(context)
            .setTitle(parcour.name)
            .setPositiveButton("Lets shoot") { _, _ ->
                val intent = Intent(context, PreGame::class.java)
                context.startActivity(intent)
            }
            .setNegativeButton("Abbrechen") {_, _ ->
            }
            .setView(dialogBinding.root)
            .create()

        holder.binding.apply {
            tvParcoursName.text = parcour.name

            cardView.setOnClickListener{
                shootDialog.show()


            }
        }
    }

    override fun getItemCount() = parcours.size
}