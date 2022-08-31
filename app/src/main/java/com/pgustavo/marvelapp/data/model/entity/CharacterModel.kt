package com.pgustavo.marvelapp.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pgustavo.marvelapp.data.model.ThumbnailModel
import java.io.Serializable

@Entity(tableName = "character")
data class CharacterModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: ThumbnailModel
): Serializable
