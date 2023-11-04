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
        private val title: TextView = itemView.findViewById(R.id.title)

        private val teamAName: TextView = itemView.findViewById(R.id.team_a_name)
        private val teamAScore: TextView = itemView.findViewById(R.id.team_a_score)

        private val teamBName: TextView = itemView.findViewById(R.id.team_b_name)
        private val teamBScore: TextView = itemView.findViewById(R.id.team_b_score)

        fun bind(game: Game) {
            title.text = game.title
            teamAName.text = game.teamA.name
            teamBName.text = game.teamB.name
            teamAScore.text = game.teamA.score.toString()
            teamBScore.text = game.teamB.score.toString()
        }
    }
}
