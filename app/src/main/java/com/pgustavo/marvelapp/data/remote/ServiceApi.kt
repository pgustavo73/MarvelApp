package com.pgustavo.marvelapp.data.remote

import com.pgustavo.marvelapp.data.model.entity.CharacterModelResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ServiceApi {

    @GET("characters")
    suspend fun list(
        @Query("nameStartsWith") nameStartsWith: String? = null
    ): Response<CharacterModelResponse>
}