package com.pdm.placar.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.pdm.placar.Game
import com.pdm.placar.MainActivity
import java.io.ByteArrayInputStream
import java.io.ObjectInputStream

class HistoryViewModel : ViewModel() {

    fun getPreviousGames(context: Context): List<Game> {
        val sharedPreferences = context.getSharedPreferences(MainActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val gameList = mutableListOf<Game>()

        val numMatches = sharedPreferences.getInt("numberMatch", 0)

        for (i in 1..numMatches) {
            val matchKey = "match$i"
            val serializedMatch = sharedPreferences.getString(matchKey, null)

            if (!serializedMatch.isNullOrBlank()) {
                val matchBytes = serializedMatch.toByteArray(Charsets.ISO_8859_1)
                val matchObject = deserializeGameObject(matchBytes)
                gameList.add(matchObject)
            }
        }

        return gameList
    }

    private fun deserializeGameObject(serializedData: ByteArray): Game {
        val byteArrayInputStream = ByteArrayInputStream(serializedData)
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        return objectInputStream.readObject() as Game
    }
}