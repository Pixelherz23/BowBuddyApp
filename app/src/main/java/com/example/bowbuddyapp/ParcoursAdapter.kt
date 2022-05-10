package com.example.bowbuddyapp


import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_parcours_card.view.*
import kotlinx.android.synthetic.main.custom_alertdialog.view.*
import android.content.Context
import android.content.DialogInterface
import android.view.View.inflate
import android.widget.Button
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_create_parcours.view.*
import kotlinx.coroutines.NonDisposableHandle.parent

//Job of adapter: creating views for our items that have in our recycler
//short: binds data to views(item_card.xml)
class ParcoursAdapter(var elem: List<ParcoursCard>, var context: Context) : RecyclerView.Adapter<ParcoursAdapter.AdapterHodl>()  {

    var alertDialogWindow = AlertDialog.Builder(context)


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
        //Log.i("Tag",elem[position].parcoursName)
        holder.itemView.tv_parcours_name.text = elem[position].parcoursName
        holder.itemView.card_view.setOnClickListener{
            Log.i("card", "card is clicked/working")
           // alertDialogWindow.setTitle(elem[position].parcoursName).setMessage("BlaBla").show()
            val builder = AlertDialog.Builder(context)
                .setTitle(elem[position].parcoursName)
                .setPositiveButton("Lets shoot", { _, _ ->
                   Log.i("btn","Positve Alert Dialog Button clicked")

                    //Todo logic
                })
                .setNegativeButton("Abbrechen", { dialog, _ ->
                    Log.i("btn","Negative Alert Dialog Button clicked")
                    //dialog.dismiss()
                })


                .create()
            val view = LayoutInflater.from(context).inflate(R.layout.custom_alertdialog,null)

            var city =elem[position].city
            var street=elem[position].street
            var price = elem[position].price.toString()
            var info =elem[position].info

            view.et_street.setText("$city, $street")
            view.et_price.setText(price)
            view.et_info_description.setText(info)
            builder.setView(view)
            builder.show()

        }

    }

    override fun getItemCount(): Int {
        return elem.size
    }
}