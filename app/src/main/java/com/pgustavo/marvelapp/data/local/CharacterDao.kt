package com.pgustavo.marvelapp.data.local

import androidx.room.*
import com.pgustavo.marvelapp.data.model.entity.CharacterModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(characterModel: CharacterModel) : Long

    @Query("SELECT * FROM character ORDER BY id")
    fun getAll(): Flow<List<CharacterModel>>

    @Delete
    suspend fun delete(characterModel: CharacterModel)
}

