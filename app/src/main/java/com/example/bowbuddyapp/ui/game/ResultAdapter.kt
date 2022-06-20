package com.example.bowbuddyapp.ui.game

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bowbuddyapp.data.PointsParcours
import com.example.bowbuddyapp.data.User
import com.example.bowbuddyapp.databinding.ItemResultBinding
import com.example.bowbuddyapp.viewModel.ResultViewModel
import io.getstream.avatarview.coil.loadImage
import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * Adapter for binding each list element of ([players]) to an item card in [ResultFragment]
 * The adapter is necessary for the recyclerview
 * @author Lukas Beckmann
 *
 * @property viewModel is responsible for the data
 * @property rule the rule with the game is played
 */
class ResultAdapter(val viewModel: ResultViewModel, val rule: String): RecyclerView.Adapter<ResultAdapter.ResultViewHolder>() {

    inner class ResultViewHolder(val binding: ItemResultBinding): RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object: DiffUtil.ItemCallback<PointsParcours>(){
        override fun areItemsTheSame(oldItem: PointsParcours, newItem: PointsParcours): Boolean =
            oldItem.email == newItem.email

        override fun areContentsTheSame(oldItem: PointsParcours, newItem: PointsParcours): Boolean =
            oldItem == newItem
    }
    private val differ = AsyncListDiffer(this, diffCallback)
    var points: List<PointsParcours>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    lateinit var players: List<User>
    lateinit var hits: Map<String, Int>



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val binding = ItemResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResultViewHolder(binding)
    }

    /**
     * binds the data for each [points] to the item card
     */
    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val points = points[position]
        var player = User("", "", "", "", 0)

        players.forEach { user ->
            if(points.email == user.email){
                player = user
            }
        }
        holder.binding.apply {
            tvName.text = player.name
            avatarView.loadImage(player.profilImage)
            val pointsMax = "${points.points}/${viewModel.getMaxPoints(rule, viewModel.getMaxHits(rule)!!)}"
            tvPointsValue.text = pointsMax
            hits.forEach { email, hits ->
                if (email == player.email) {
                    val df = DecimalFormat("#.##")
                    df.roundingMode = RoundingMode.DOWN
                    val hitsMax = "${hits}/${viewModel.getMaxHits(rule)}"
                    tvHitsValue.text = hitsMax
                    val hitProbability =  "${df.format(viewModel.getHitProbability(viewModel.getMaxHits(rule)!!, player.email))}%"
                    tvPrecisionValue.text = hitProbability
                }
            }
        }
    }

    override fun getItemCount() = points.size


}
