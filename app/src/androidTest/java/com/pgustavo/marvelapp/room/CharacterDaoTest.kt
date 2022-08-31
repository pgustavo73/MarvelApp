package com.pgustavo.marvelapp.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pgustavo.marvelapp.data.local.CharacterDao
import com.pgustavo.marvelapp.data.local.DataBase
import com.pgustavo.marvelapp.data.model.ThumbnailModel
import com.pgustavo.marvelapp.data.model.entity.CharacterModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class CharacterDaoTest {

    private lateinit var characterDao: CharacterDao
    private lateinit var db: DataBase


    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, DataBase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        characterDao = db.characterDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(IOException::class)
    fun insertAndGetAllCharacter() = runBlocking {
        val thumbnail = ThumbnailModel(path = "picture", extension = "jpg")
        val character = CharacterModel(id=1, name="test", description = "testDescription",thumbnail )
        characterDao.insert(character)
        val thumbnail2 = ThumbnailModel(path = "picture1", extension = "jpg")
        val character2 = CharacterModel(id=2, name="test1", description = "testDescription1",thumbnail2 )
        characterDao.insert(character2)
        val allCharacter = characterDao.getAll().first()
        assertEquals(allCharacter[0].name, character.name)
        assertEquals(allCharacter[0].description, character.description)
        assertEquals(allCharacter[0].thumbnail, character.thumbnail)
        assertEquals(allCharacter[1].name, character2.name)
        assertEquals(allCharacter[1].description, character2.description)
        assertEquals(allCharacter[1].thumbnail, character2.thumbnail)
    }

    @Test
    @Throws(IOException::class)
    fun insertAndDelete() = runBlocking {
        val thumbnail = ThumbnailModel(path = "test", extension = "jpg")
        val character = CharacterModel(id=1, name="test", description = "testDescription",thumbnail )
        characterDao.insert(character)
        characterDao.delete(character)
        val allCharacter = characterDao.getAll().first()
        assertEquals(allCharacter.size, 0)
    }



}