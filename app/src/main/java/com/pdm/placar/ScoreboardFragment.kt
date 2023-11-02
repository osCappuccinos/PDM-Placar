package com.pdm.placar

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.pdm.placar.databinding.FragmentScoreboardBinding
import com.pdm.placar.viewmodels.ScoreboardViewModel

class ScoreboardFragment : Fragment() {

    private lateinit var viewModel: ScoreboardViewModel

    private lateinit var binding: FragmentScoreboardBinding


    private var handler = Handler(Looper.getMainLooper())
    private var seconds = 0
    private var isTimerRunning = false

    private val timerRunnable = object : Runnable {
        override fun run() {
            if (isTimerRunning) {
                seconds++
                updateTimerText()
                handler.postDelayed(this, 1000) // Run this Runnable every second
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity()).get(ScoreboardViewModel::class.java)

        binding = FragmentScoreboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateTeamsNames()
        updateScoreboard()
        binding.menuButton.setOnClickListener { showPopupMenu(binding.menuButton) }
        onLeftSideClicked()
        onRightSideClicked()
        startTimer()
    }

    private fun updateTeamsNames() {
        val newNameA = requireActivity().intent.getStringExtra("teamAName")
        val newNameB = requireActivity().intent.getStringExtra("teamBName")
        newNameA?.let { viewModel.teamA.name = it }
        newNameB?.let { viewModel.teamB.name = it }
    }

    private fun updateScoreboard() {
        binding.teamAName.text = viewModel.teamA.name
        binding.teamAScore.text = viewModel.teamA.score.toString()

        binding.teamBName.text = viewModel.teamB.name
        binding.teamBScore.text = viewModel.teamB.score.toString()
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.action_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_start_stop -> {
                    viewModel.saveGameResult(viewModel.game)
                    return@setOnMenuItemClickListener true
                }
                R.id.action_undo -> {
                    viewModel.undoScoreIncrease()
                    updateScoreboard()
                    return@setOnMenuItemClickListener true
                }
                R.id.action_config -> {
                    val intent = Intent(requireContext(), SettingsActivity::class.java)
                    startActivity(intent)
                    return@setOnMenuItemClickListener true
                }
                else -> return@setOnMenuItemClickListener false
            }
        }
        popupMenu.show()
    }

    private fun onLeftSideClicked() {
        binding.leftSide.setOnClickListener {
            viewModel.increaseScoreBy1(viewModel.teamA)
            val currentScore = viewModel.teamA.score
            binding.teamAScore.text = (currentScore).toString()
        }
    }

    private fun onRightSideClicked() {
        binding.rightSide.setOnClickListener {
            viewModel.increaseScoreBy1(viewModel.teamB)
            val currentScore = viewModel.teamB.score
            binding.teamBScore.text = (currentScore).toString()
        }
    }

    private fun startTimer() {
        isTimerRunning = true
        handler.post(timerRunnable)
    }

    private fun stopTimer() {
        isTimerRunning = false
        handler.removeCallbacks(timerRunnable)
    }

//    private fun resetTimer() {
//        isTimerRunning = false
//        handler.removeCallbacks(timerRunnable)
//        seconds = 0
//        updateTimerText()
//    }

    private fun updateTimerText() {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        val timeText = String.format("%d:%02d", minutes, remainingSeconds)
        binding.timer.text = timeText
    }

    companion object {
        fun newInstance() = ScoreboardFragment()
    }
}