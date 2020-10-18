package com.example.madlevel4task2.dao

import androidx.room.*
import com.example.madlevel4task2.model.Game

@Dao
interface GameDao {

    @Query("SELECT * FROM gamesTable")
    suspend fun getAllGames(): List<Game>

    @Insert
    suspend fun insertGame(game: Game)

    @Query("DELETE From gamesTable")
    suspend fun deleteAllGames()
}