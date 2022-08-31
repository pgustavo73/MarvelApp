package com.pgustavo.marvelapp.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.pgustavo.marvelapp.data.model.ThumbnailModel

class ThumbnailConverters {

    @TypeConverter
    fun fromThumbnail(thumbnailModel: ThumbnailModel): String = Gson().toJson(thumbnailModel)

    @TypeConverter
    fun thumbnailModel(thumbnailModel: String): ThumbnailModel =
        Gson().fromJson(thumbnailModel, ThumbnailModel::class.java)
}