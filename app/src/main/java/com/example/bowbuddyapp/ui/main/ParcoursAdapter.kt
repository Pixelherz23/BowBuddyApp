package com.example.bowbuddyapp.ui.main


import android.app.AlertDialog
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bowbuddyapp.data.Game
import com.example.bowbuddyapp.data.Parcours
import com.example.bowbuddyapp.databinding.CustomAlertdialogPregameBinding
import com.example.bowbuddyapp.databinding.CustomAlertdialogBinding
import com.example.bowbuddyapp.databinding.ItemParcoursCardBinding
import com.example.bowbuddyapp.ui.game.GameActivity
import com.example.bowbuddyapp.viewModel.ParcoursViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

//Job of adapter: creating views for our items that have in our recycler
//short: binds data to views(item_card.xml)
class ParcoursAdapter(private val viewModel: ParcoursViewModel) : RecyclerView.Adapter<ParcoursAdapter.ParcourViewHolder>() {

    //holds the views of the items that are currently displayed
    inner class ParcourViewHolder(val binding: ItemParcoursCardBinding) : RecyclerView.ViewHolder(binding.root)

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface MyEntryPoint {
      fun getGoogleAcc() : GoogleSignInAccount
    }

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
        val preGameBinding = CustomAlertdialogPregameBinding.inflate(LayoutInflater.from(context))
        var rule: String = ""
        var multiplayerFlag = false

        dialogBinding.apply {
            etStreet.text = parcour.address
            etPrice.text = parcour.price
            etInfoDescription.text = parcour.info
        }

        val preGameDialog = AlertDialog.Builder(context)
            .setTitle(parcour.name)
            .setPositiveButton("Lets shoot"){ _, _ ->
                viewModel.game.value = Game(parcour.id, viewModel.link.value!!, rule)
                // Datenbankenspam verhindern
                //viewModel.sendGame("test@api.com")
                val intent = Intent(context, GameActivity::class.java)

                //intent.putExtra("link", viewModel.link.value)
                intent.putExtra("link", "https://bowbuddy.com/onsz4qLZ76")
                intent.putExtra("multiplayer", multiplayerFlag)
                context.startActivity(intent)
            }
            .setNegativeButton("Abbrechen") {_, _ ->
            }
            .setView(preGameBinding.root)
            .create()

        val shootDialog = AlertDialog.Builder(context)
            .setTitle(parcour.name)
            .setPositiveButton("Weiter") { _, _ ->
                preGameDialog.show()
            }
            .setNegativeButton("Abbrechen") {_, _ ->
            }
            .setView(dialogBinding.root)
            .create()

        val deleteDialog = AlertDialog.Builder(context)
            .setTitle("Parcour wirklich löschen")
            .setPositiveButton("Ja") { _, _ ->
                GlobalScope.launch() {

                    var x = viewModel.deleteParcours()
                    x.join()

                    val myEntryPoint = EntryPointAccessors.fromApplication(context, MyEntryPoint::class.java)

                    viewModel.fetchData(myEntryPoint.getGoogleAcc().email.toString())

                }


            }
            .setNegativeButton("Nein") {_, _ ->
            }
            .create()

        preGameBinding.apply {
            spRule.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(adapter: AdapterView<*>?, view: View?, indexOfItem: Int, idOfItem: Long) {
                     rule = adapter?.getItemAtPosition(indexOfItem).toString()
                    Log.i("Item", rule)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }

            swMultiplayer.setOnCheckedChangeListener{ _, isChecked ->
                multiplayerFlag = isChecked
                if(isChecked){
                    tvLink.text = viewModel.link.value
                    preGameBinding.linearLayout.visibility = View.VISIBLE
                }else{
                    preGameBinding.linearLayout.visibility = View.GONE
                }
            }

            imShare.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND)
                intent.setType("text/plain")
                intent.putExtra(Intent.EXTRA_SUBJECT, "URL zum Teilen")
                intent.putExtra(Intent.EXTRA_TEXT, tvLink.text)
                context.startActivity(Intent.createChooser(intent, "Share URL"))
            }

            holder.binding.apply {
                tvParcoursName.text = parcour.name

                cardView.setOnClickListener{

                    shootDialog.show()
                    viewModel.generateLink()

                }

                cardView.setOnLongClickListener{
                    deleteDialog.show()
                    viewModel.parcoursIdTodelete.value = parcour.id.toString()

                    return@setOnLongClickListener true
                }

            }
        }
    }


    override fun getItemCount() = parcours.size

}
