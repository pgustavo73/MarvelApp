package com.pgustavo.marvelapp.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pgustavo.marvelapp.data.model.entity.CharacterModel
import com.pgustavo.marvelapp.repository.MarvelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository: MarvelRepository): ViewModel() {

    fun insert(characterModel: CharacterModel) = viewModelScope.launch{
        repository.insert(characterModel)
    }

}