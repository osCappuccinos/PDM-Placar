package com.pdm.placar

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.pdm.placar.databinding.FragmentScoreboardBinding
import com.pdm.placar.viewmodels.ScoreboardViewModel

class ScoreboardFragment : Fragment() {
    private lateinit var viewModel: ScoreboardViewModel
    private lateinit var binding: FragmentScoreboardBinding
    private lateinit var sharedPreferences: SharedPreferences

    private val timerManager = TimerManager { updateTimerText() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScoreboardBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity()).get(ScoreboardViewModel::class.java)

        sharedPreferences = requireContext().getSharedPreferences(MainActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val seconds = sharedPreferences.getInt(TimerManager.TIMER_KEY, 0)
        val isTimerRunning = sharedPreferences.getBoolean(TimerManager.TIMER_RUNNING_KEY, false)
        timerManager.restoreTimerState(seconds, isTimerRunning)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startTimer()
        updateTimerText()
        updateTeamsNames()
        updateScoreboard()
        updateTimeConfig()

        onTimeClicked()
        onMenuClicked()
        onLeftSideClicked()
        onRightSideClicked()
    }

    override fun onPause() {
        super.onPause()

        sharedPreferences
            .edit()
            .putInt(TimerManager.TIMER_KEY, timerManager.getTimerValue())
            .putBoolean(TimerManager.TIMER_RUNNING_KEY, timerManager.isTimerRunning())
            .apply()
    }

    override fun onDestroy() {
        super.onDestroy()

        sharedPreferences
            .edit()
            .putInt(TimerManager.TIMER_KEY, timerManager.getTimerValue())
            .putBoolean(TimerManager.TIMER_RUNNING_KEY, timerManager.isTimerRunning())
            .apply()
    }

    private fun updateTeamsNames() {
        val newNameA = requireActivity().intent.getStringExtra(SettingsActivity.TEAM_A_NAME)
        val newNameB = requireActivity().intent.getStringExtra(SettingsActivity.TEAM_B_NAME)
        newNameA?.let { viewModel.teamA.name = it }
        newNameB?.let { viewModel.teamB.name = it }
    }

    private fun updateScoreboard() {
        binding.teamAName.text = viewModel.teamA.name
        binding.teamAScore.text = viewModel.teamA.score.toString()

        binding.teamBName.text = viewModel.teamB.name
        binding.teamBScore.text = viewModel.teamB.score.toString()
    }

    private fun onMenuClicked() {
        binding.menuButton.setOnClickListener { showPopupMenu(binding.menuButton) }
    }

    private fun onTimeClicked() {
        binding.timer.setOnClickListener { showTimePopupMenu(binding.timer) }
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

    private fun showTimePopupMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.timer_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_start_timer -> {
                    if (!timerManager.isTimerRunning()) {
                        startTimer()
                    }
                    return@setOnMenuItemClickListener true
                }
                R.id.action_stop_timer -> {
                    stopTimer()
                    return@setOnMenuItemClickListener true
                }
                R.id.action_reset_timer -> {
                    resetTimer()
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
        timerManager.startTimer()
    }

    private fun stopTimer() {
        timerManager.stopTimer()
    }

    private fun resetTimer() {
        timerManager.resetTimer()
    }

    private fun updateTimerText() {
        val minutes = timerManager.getTimerValue() / 60
        val remainingSeconds = timerManager.getTimerValue() % 60
        val timeText = String.format("%d:%02d", minutes, remainingSeconds)
        binding.timer.text = timeText

        val isSecondHalf = timerManager.isSecondHalf()
        if (isSecondHalf) {
            binding.half.text = "2º"
        } else {
            binding.half.text = "1º"
        }
    }

    private fun updateTimeConfig() {
        val extraTime = requireActivity().intent.getStringExtra(SettingsActivity.EXTRA_TIME)
        val extraSeconds = if (!extraTime.isNullOrEmpty()) extraTime.toInt().times(60) else 0
        timerManager.setupExtraTime(extraSeconds)
        if (!extraTime.isNullOrEmpty()) {
            val formattedExtraTime = "+$extraTime"
            binding.extraTime.text = formattedExtraTime
        }
    }

    companion object {
        fun newInstance() = ScoreboardFragment()
    }
}