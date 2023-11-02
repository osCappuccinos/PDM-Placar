package com.pdm.placar.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.pdm.placar.Game
import com.pdm.placar.MainActivity
import com.pdm.placar.Team
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream
import java.nio.charset.StandardCharsets

class ScoreboardViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPreferences = application.getSharedPreferences(MainActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE)
    private val scoreStack: ArrayDeque<Pair<String, Int>> = ArrayDeque()
    val teamA = Team(name = "teamA", score = 0)
    val teamB = Team(name = "teamB", score = 0)
    val game = Game(teamA = teamA, teamB = teamB)

    fun increaseScoreBy1(team: Team) {
            team.score += 1
            scoreStack.addFirst(Pair(team.name, team.score))
    }

    fun undoScoreIncrease() {
        if (scoreStack.isNotEmpty()) {
            val (team, score) = scoreStack.removeFirst()
            when (team) {
                teamA.name -> teamA.score = score - 1
                teamB.name -> teamB.score = score - 1
            }
        }
    }

    fun saveGameResult(game: Game) {
        val numMatches = sharedPreferences.getInt("numberMatch", 0) + 1
        sharedPreferences
            .edit()
            .putInt("numberMatch", numMatches)
            .apply()

        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(game)

        sharedPreferences
            .edit()
            .putString("match$numMatches", byteArrayOutputStream.toString(StandardCharsets.ISO_8859_1.name()))
            .apply()
    }
}