package com.pgustavo.marvelapp.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pgustavo.marvelapp.data.model.entity.CharacterModel
import com.pgustavo.marvelapp.repository.MarvelRepository
import com.pgustavo.marvelapp.state.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val repository: MarvelRepository): ViewModel(){

    private val _favorite = MutableStateFlow<ResourceState<List<CharacterModel>>>(ResourceState.Empty())
    val favorites: StateFlow<ResourceState<List<CharacterModel>>> = _favorite

    init {
        fetch()
    }

    private fun fetch() = viewModelScope.launch {
        repository.getAll().collectLatest { characters ->
            if(characters.isNullOrEmpty()) {
                _favorite.value = ResourceState.Empty()
            } else {
                _favorite.value = ResourceState.Success(characters)
            }
        }
    }

    fun delete(characterModel: CharacterModel) = viewModelScope.launch {
        repository.delete(characterModel)
    }
}