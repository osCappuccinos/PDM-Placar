package com.pdm.placar.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.pdm.placar.Game
import com.pdm.placar.R

class GameAdapter(private val gameList: List<Game>) :
    RecyclerView.Adapter<GameAdapter.GameViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_game, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = gameList[position]
        holder.bind(game)
    }

    override fun getItemCount(): Int {
        return gameList.size
    }

    inner class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val teamATextView: TextView = itemView.findViewById(R.id.teamATextView)
        private val teamBTextView: TextView = itemView.findViewById(R.id.teamBTextView)

        fun bind(game: Game) {
            val teamAResult = "${game.teamA.name}${game.teamA.score}"
            val teamBResult = "${game.teamB.name}${game.teamB.score}"
            teamATextView.text = teamAResult
            teamBTextView.text = teamBResult
        }
    }
}
