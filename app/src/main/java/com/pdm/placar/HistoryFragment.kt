package com.pdm.placar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pdm.placar.adapters.GameAdapter
import com.pdm.placar.viewmodels.HistoryViewModel

class HistoryFragment : Fragment() {
    private lateinit var viewModel: HistoryViewModel
    private lateinit var gameAdapter: GameAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity()).get(HistoryViewModel::class.java)

        val view = inflater.inflate(R.layout.fragment_history_list, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.list)

        val gameList = viewModel.getPreviousGames(requireContext())

        gameAdapter = GameAdapter(gameList)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = gameAdapter

        return view
    }
}