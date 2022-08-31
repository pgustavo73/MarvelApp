package com.pgustavo.marvelapp.repository

import com.pgustavo.marvelapp.data.local.CharacterDao
import com.pgustavo.marvelapp.data.model.entity.CharacterModel
import com.pgustavo.marvelapp.data.remote.ServiceApi
import javax.inject.Inject

class MarvelRepository @Inject constructor(private val api: ServiceApi,
private val dao: CharacterDao
){
    suspend fun list(nameStartsWith: String? = null) = api.list(nameStartsWith)

    suspend fun insert(characterModel: CharacterModel) = dao.insert(characterModel)
    fun getAll() = dao.getAll()
    suspend fun delete(characterModel: CharacterModel) = dao.delete(characterModel)
}