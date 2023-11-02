package com.pdm.placar

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AppCompatActivity
import com.pdm.placar.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE)

        restoreFields()
        onSaveClicked()
    }

    private fun onSaveClicked() {
        binding.saveButton.setOnClickListener {
            val teamA = binding.teamANameEditText.text.toString()
            val teamB = binding.teamBNameEditText.text.toString()
            val extraTime = binding.extraTime.text.toString()
            val matchName = binding.matchNameEditText.text.toString()

            sharedPreferences
                .edit()
                .putString(TEAM_A_NAME, teamA)
                .putString(TEAM_B_NAME, teamB)
                .putString(EXTRA_TIME, extraTime)
                .putString(MATCH_NAME, matchName)
                .apply()

            val intent = Intent(this, MainActivity::class.java)

            intent.putExtra(TEAM_A_NAME, teamA)
            intent.putExtra(TEAM_B_NAME, teamB)
            intent.putExtra(EXTRA_TIME, extraTime)
            intent.putExtra(MATCH_NAME, matchName)

            startActivity(intent)
            finish()
        }
    }

    private fun restoreFields() {
        val teamAName = sharedPreferences.getString(TEAM_A_NAME, "")
        binding.teamANameEditText.text = Editable.Factory.getInstance().newEditable(teamAName)

        val teamBName = sharedPreferences.getString(TEAM_B_NAME, "")
        binding.teamBNameEditText.text = Editable.Factory.getInstance().newEditable(teamBName)

        val extraTime = sharedPreferences.getString(EXTRA_TIME, "")
        binding.extraTime.text = Editable.Factory.getInstance().newEditable(extraTime)

        val matchName = sharedPreferences.getString(MATCH_NAME, "") // Retrieve the match name
        binding.matchNameEditText.text = Editable.Factory.getInstance().newEditable(matchName)
    }


    companion object {
        const val TEAM_A_NAME = "TEAM_A_NAME"
        const val TEAM_B_NAME = "TEAM_B_NAME"
        const val EXTRA_TIME = "EXTRA_TIME"
        const val MATCH_NAME = "MATCH_NAME" // Key for the match name
    }
}