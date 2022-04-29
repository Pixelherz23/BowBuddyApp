package com.example.bowbuddyapp


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_parcours_card.view.*


//Job of adapter: creating views for our items that have in our recycler
//short: binds data to views(item_card.xml)
class ParcoursAdapter(var elem: List<ParcoursCard>) : RecyclerView.Adapter<ParcoursAdapter.AdapterHodl>()  {


    //holds the views of the items that are currently displayed
    inner class AdapterHodl(itemView: View): RecyclerView.ViewHolder(itemView)

    //called when the recycler needs a new view holder (for wexample when scrolling)
    //viewGroup: collection of views. (LinearLayout for example)
    //parent = the recylerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterHodl {
        //"inflates" the item_card.xml file, so we can access it with kotlin
        // val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent,false)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_parcours_card, parent,false)
        return AdapterHodl(view)
    }
    //binding the data (elem) to our views/items
    //adding functionality
    override fun onBindViewHolder(holder: AdapterHodl, position: Int) {
        /*
        holder.itemView.apply {
            info_text.text = elem[position].title
            card_view.setOnClickListener{

                Log.i("card", "card is clicked/working")
            }
        }

         */

        holder.itemView.tv_parcours_name.text = elem[position].parcoursName
        holder.itemView.card_view.setOnClickListener{
            Log.i("card", "card is clicked/working")


        }

    }

    override fun getItemCount(): Int {
        return elem.size
    }
}