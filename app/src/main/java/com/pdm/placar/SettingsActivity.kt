package com.pdm.placar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val teamANameEditText: EditText = findViewById(R.id.teamANameEditText)
        val teamBNameEditText: EditText = findViewById(R.id.teamBNameEditText)
        val saveButton: Button = findViewById(R.id.saveButton)

        saveButton.setOnClickListener {
            val teamA = teamANameEditText.text.toString()
            val teamB = teamBNameEditText.text.toString()

            val intent = Intent(this, MainActivity::class.java)

            intent.putExtra("teamAName", teamA)
            intent.putExtra("teamBName", teamB)

            startActivity(intent)
            finish()
        }
    }
}