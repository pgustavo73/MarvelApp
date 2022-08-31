package com.pgustavo.marvelapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pgustavo.marvelapp.data.model.entity.CharacterModel

@Database(entities = [CharacterModel::class], version = 1, exportSchema = false)
@TypeConverters(ThumbnailConverters::class)
abstract class DataBase: RoomDatabase() {

    abstract fun characterDao(): CharacterDao
}