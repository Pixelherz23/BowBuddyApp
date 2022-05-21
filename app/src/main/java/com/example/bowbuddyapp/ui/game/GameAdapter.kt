package com.example.bowbuddyapp.ui.game

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bowbuddyapp.databinding.ItemTargetCardBinding
import com.example.bowbuddyapp.data.Target
import com.example.bowbuddyapp.databinding.CustomAlertdialogPointsBinding

class GameAdapter () : RecyclerView.Adapter<GameAdapter.TargetViewHolder>() {
    //numpicker 0,1,2,3,4
    val dsb = arrayOf("11","10","8","5","0")

    val firstArrow = arrayOf("20","18","0")
    val secondArrow = arrayOf("16","14","0")
    val thirdArrow = arrayOf("12","10","0")
    val dfbv = arrayOf(firstArrow, secondArrow, thirdArrow)





    //holds the views of the items that are currently displayed
    inner class TargetViewHolder(val binding: ItemTargetCardBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object: DiffUtil.ItemCallback<Target>() {
        override fun areItemsTheSame(oldItem: Target, newItem: Target): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Target, newItem: Target): Boolean =
            oldItem == newItem
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var targets: List<Target>
        get() = differ.currentList
        set(value) {differ.submitList(value)}


    //called when the recycler needs a new view holder (for wexample when scrolling)
    //viewGroup: collection of views. (LinearLayout for example)
    //parent = the recylerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TargetViewHolder {
        //"inflates" the item_card.xml file, so we can access it with kotlin
        // val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent,false)
        val binding = ItemTargetCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TargetViewHolder(binding)
    }

    //binding the data (elem) to our views/items
    //adding functionality
    override fun onBindViewHolder(holder: TargetViewHolder, position: Int) {
        val target = targets[position]
        val context = holder.itemView.context
        val dialogBinding = CustomAlertdialogPointsBinding.inflate(LayoutInflater.from(context))


        val pointsDialog = AlertDialog.Builder(context)
            .setTitle(target.name)



        pointsDialog.setPositiveButton("Speichern") { _, _ ->
                dialogBinding
            }

        holder.binding.apply {
            tvTargetName.text = target.name
            cardViewTarget.setOnClickListener {
                pointsDialog.show()
            }
        }
    }

    override fun getItemCount() = targets.size
    /*
    view.lvl1.numberPicker_one.minValue = 0
    view.lvl1.numberPicker_one.maxValue = dsb.size-1
    view.lvl1.numberPicker_one.displayedValues = dsb


    view.lvl1.numberPicker_one.wrapSelectorWheel = false
    view.lvl1.numberPicker_one.setOnValueChangedListener{ numberPicker, oldVal, newVal ->
        Log.i("Numpicker","$newVal")

    }
    */
            //linearLayout_alertDialog_points



    fun convertSpinnerData(spinnerPoints: IntArray, gameRule: String ): IntArray {
        var intArr = IntArray(3)

        if (gameRule.equals("DSB (WA)")) {
            //val dsb = arrayOf("11","10","8","5","0")
            // 11 = 0, 10 = 1, 8 = 2

            var i = 0
            for (x in spinnerPoints) {
                if (x == 0) {
                    intArr[i] = 11
                }
                if (x == 1) {
                    intArr[i] = 10
                }
                if (x == 2) {
                    intArr[i] = 8
                }
                if (x == 3) {
                    intArr[i] = 5
                }
                if (x == 4) {
                    intArr[i] = 0
                }

                i++
            }
        } else if (gameRule.equals("DFBV (IFAA)")) {

            if (spinnerPoints[0] == 0) {
                intArr[0] = 20
            } else if (spinnerPoints[0] == 1) {
                intArr[0] = 18
            } else {
                intArr[0] = 0
            }

            if (spinnerPoints[1] == 0) {
                intArr[1] = 16
            } else if (spinnerPoints[1] == 1) {
                intArr[1] = 14
            } else {
                intArr[1] = 0
            }

            if (spinnerPoints[2] == 0) {
                intArr[2] = 12
            } else if (spinnerPoints[2] == 1) {
                intArr[2] = 10
            } else {
                intArr[2] = 0
            }


        }

        return intArr

    }
}