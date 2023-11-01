package com.pdm.placar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.pdm.placar.viewmodels.ScoreboardViewModel

class ScoreboardFragment : Fragment() {

    private lateinit var viewModel: ScoreboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity()).get(ScoreboardViewModel::class.java)

        return inflater.inflate(R.layout.fragment_scoreboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateTeamsNames()
        updateScoreboard()

        val menuButton = view.findViewById<Button>(R.id.menuButton)
        menuButton.setOnClickListener { showPopupMenu(menuButton) }

        val leftSide: ConstraintLayout = view.findViewById(R.id.left_side)
        val rightSide: ConstraintLayout = view.findViewById(R.id.right_side)
        onLeftSideClicked(leftSide)
        onRightSideClicked(rightSide)
    }

    private fun updateTeamsNames() {
        val newNameA = requireActivity().intent.getStringExtra("teamAName")
        val newNameB = requireActivity().intent.getStringExtra("teamBName")
        newNameA?.let { viewModel.teamA.name = it }
        newNameB?.let { viewModel.teamB.name = it }
    }

    private fun updateScoreboard() {
        val teamAName: TextView? = view?.findViewById(R.id.teamAName)
        teamAName?.text = viewModel.teamA.name

        val teamAScore: TextView? = view?.findViewById(R.id.teamAScore)
        teamAScore?.text = viewModel.teamA.score.toString()

        val teamBName: TextView? = view?.findViewById(R.id.teamBName)
        teamBName?.text = viewModel.teamB.name

        val teamBScore: TextView? = view?.findViewById(R.id.teamBScore)
        teamBScore?.text = viewModel.teamB.score.toString()
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

    private fun onLeftSideClicked(leftSide: ConstraintLayout) {
        leftSide.setOnClickListener {
            val teamAScore = view?.findViewById<TextView>(R.id.teamAScore)
            viewModel.increaseScoreBy1(viewModel.teamA)
            val currentScore = viewModel.teamA.score
            teamAScore?.text = (currentScore).toString()
        }
    }

    private fun onRightSideClicked(rightSide: ConstraintLayout) {
        rightSide.setOnClickListener {
            val teamBScore = view?.findViewById<TextView>(R.id.teamBScore)
            viewModel.increaseScoreBy1(viewModel.teamB)
            val currentScore = viewModel.teamB.score
            teamBScore?.text = (currentScore).toString()
        }
    }

    companion object {
        fun newInstance() = ScoreboardFragment()
    }
}