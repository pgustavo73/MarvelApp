package com.pgustavo.marvelapp.api

import com.pgustavo.marvelapp.data.model.entity.CharacterModel
import com.pgustavo.marvelapp.data.remote.ServiceApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.IOException


@RunWith(JUnit4::class)
class ApiServiceTest : BaseServiceTest<ServiceApi>() {

    private lateinit var service : ServiceApi
    private lateinit var results: CharacterModel

    @Before
    fun initService() {
        service = createService(ServiceApi::class.java)
    }

    @Throws(IOException::class)
    @Test
    fun fetchApiListTest()   {
        enqueueResponse("Response.json")
        runBlocking {
            results = requireNotNull(service.list().body()?.data?.results?.get(0))
        }
        mockWebServer.takeRequest()
        Assert.assertEquals(results.name, "3-D Man")
    }
}