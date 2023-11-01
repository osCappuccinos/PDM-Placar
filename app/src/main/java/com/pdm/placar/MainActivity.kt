package com.pdm.placar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigateToScoreboard()
        setupBottomNav()
    }

    private fun setupBottomNav() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_score -> {
                    navigateToScoreboard()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_history -> {
                    navigateToHistory()
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
        }
    }

    private fun navigateToScoreboard() {
        val fragment = ScoreboardFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun navigateToHistory() {
        val fragment = HistoryFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    companion object {
        const val SHARED_PREFERENCES = "sharedPreferences"
    }
}