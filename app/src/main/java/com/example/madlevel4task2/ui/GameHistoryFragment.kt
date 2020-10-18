package com.example.madlevel4task2.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madlevel4task2.R
import com.example.madlevel4task2.model.Game
import com.example.madlevel4task2.repository.GameRepository
import kotlinx.android.synthetic.main.fragment_game_history.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class GameHistoryFragment : Fragment() {

    private lateinit var gameRepository: GameRepository

    private var gamesList = arrayListOf<Game>()
    private var gameHistoryAdapter = GameHistoryAdapter(gamesList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gameRepository = GameRepository(requireContext())
        initViews()
        getGamesFromDatabase()

        /*ivDeleteAll.setOnClickListener() {
            deleteAllGames()
        }

        toolbarHistory.setNavigationOnClickListener() {
            findNavController().popBackStack()
        }*/
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }

        return when (item.itemId) {
            R.id.action_clear -> {
                deleteAllGames()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onBackPressed() {
        findNavController().popBackStack()
    }

    private fun deleteAllGames() {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                gameRepository.deleteAllGames()
            }
            getGamesFromDatabase()
            gameHistoryAdapter.notifyDataSetChanged()
        }
    }

    private fun initViews() {
        // Initialize the recycler view with a linear layout manager, adapter
        rvGameHistory.layoutManager = LinearLayoutManager(context)
        rvGameHistory.adapter = gameHistoryAdapter
        gameHistoryAdapter.notifyDataSetChanged()
    }

    private fun getGamesFromDatabase() {
        CoroutineScope(Dispatchers.Main).launch {
            val games = withContext(Dispatchers.IO) {
                gameRepository.getAllGames()
            }
            this@GameHistoryFragment.gamesList.clear()
            this@GameHistoryFragment.gamesList.addAll(games)
            gameHistoryAdapter.notifyDataSetChanged()
        }
    }
}