package com.example.bowbuddyapp.ui.game

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.get
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bowbuddyapp.data.Parcours
import com.example.bowbuddyapp.data.Points
import com.example.bowbuddyapp.databinding.ItemTargetCardBinding
import com.example.bowbuddyapp.data.Target
import com.example.bowbuddyapp.databinding.CustomAlertdialogPointsDfbvBinding
import com.example.bowbuddyapp.databinding.CustomAlertdialogPointsDsbBinding
import com.example.bowbuddyapp.ui.main.ParcoursAdapter
import com.example.bowbuddyapp.viewModel.StationViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

class TargetAdapter(val viewModel: StationViewModel, val rule: String, val link: String): RecyclerView.Adapter<TargetAdapter.TargetViewHolder>(){

    inner class TargetViewHolder(val binding: ItemTargetCardBinding): RecyclerView.ViewHolder(binding.root)

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface MyEntryPoint {
        fun getGoogleAcc() : GoogleSignInAccount
    }

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
        val myEntryPoint = EntryPointAccessors.fromApplication(context, MyEntryPoint::class.java)

        val pointsDialog = if(rule == "DSB (WA)"){
            val dialogBinding = CustomAlertdialogPointsDsbBinding.inflate(LayoutInflater.from(context))
            dialogBinding.apply {
                npFirstArrow.apply {
                    minValue = 0
                    maxValue = getDsbValues().size-1
                    displayedValues = getDsbValues()
                }
                npSecondArrow.apply {
                    minValue = 0
                    maxValue = getDsbValues().size-1
                    displayedValues = getDsbValues()
                }

            }
            AlertDialog.Builder(context)
                .setTitle(target.name)
                .setPositiveButton("Speichern") { _, _ ->
                    val displayedValues = getDsbValues()
                    val firsArrow = dialogBinding.npFirstArrow.value
                    val secondArrow = dialogBinding.npSecondArrow.value
                    val points = displayedValues[firsArrow].toInt() + displayedValues[secondArrow].toInt()
                    Log.i("TA", Points(points, link, target.id, myEntryPoint.getGoogleAcc().email.toString()).toString())
                    viewModel.sendPoints(Points(points, link, target.id, myEntryPoint.getGoogleAcc().email.toString()), position)

                }
                .setNegativeButton("Abbrechen") { _, _ ->
                }
                .setView(dialogBinding.root)
                .create()

        } else {
            val dialogBinding = CustomAlertdialogPointsDfbvBinding.inflate(LayoutInflater.from(context))
            dialogBinding.apply {
                numberPicker.minValue = 0
                numberPicker.maxValue = getDfbvValues(1).size-1
                numberPicker.displayedValues = getDfbvValues(1)
                radioGroup.setOnCheckedChangeListener { radioGroup, id ->
                    when (id){
                        dialogBinding.rbFirstTry.id -> numberPicker.displayedValues = getDfbvValues(1)
                        dialogBinding.rbSecondTry.id -> numberPicker.displayedValues = getDfbvValues(2)
                        else -> numberPicker.displayedValues = getDfbvValues(3)
                    }
                }
            }
            AlertDialog.Builder(context)
                .setTitle(target.name)
                .setPositiveButton("Speichern") { _, _ ->
                    val radioId = dialogBinding.radioGroup.checkedRadioButtonId
                    val displayedValues = when (radioId){
                        dialogBinding.rbFirstTry.id -> getDfbvValues(1)
                        dialogBinding.rbSecondTry.id -> getDfbvValues(2)
                        else -> getDfbvValues(3)
                    }

                    val points = displayedValues[dialogBinding.numberPicker.value].toInt()
                    Log.i("TA", Points(points, link, target.id, myEntryPoint.getGoogleAcc().email.toString()).toString())
                    viewModel.sendPoints(Points(points, link, target.id, myEntryPoint.getGoogleAcc().email.toString()), position)
                }
                .setNegativeButton("Abbrechen") { _, _ ->
                }
                .setView(dialogBinding.root)
                .create()
        }

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


    fun getDsbValues() = arrayOf("11", "10", "8", "5", "0")
    fun getDfbvValues(arrow: Int) = arrayOf((24-arrow*4).toString(), (22-arrow*4).toString())

    override fun getItemCount() = targets.size
}