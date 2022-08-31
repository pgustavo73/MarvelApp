package com.pgustavo.marvelapp.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pgustavo.marvelapp.data.model.entity.CharacterModelResponse
import com.pgustavo.marvelapp.repository.MarvelRepository
import com.pgustavo.marvelapp.state.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(private val repository: MarvelRepository): ViewModel() {

    private val _list = MutableStateFlow<ResourceState<CharacterModelResponse>>(ResourceState.Loading())
    val list: StateFlow<ResourceState<CharacterModelResponse>> = _list

    init {
        fetch()
    }

    private fun fetch() = viewModelScope.launch {
        safeFetch()
    }

    private suspend fun safeFetch() {
        try {
            val response = repository.list()
            _list.value = handleList(response)
        } catch (t: Throwable) {
            when (t) { is IOException -> _list.value = ResourceState.Error("Internet Connection Error")
            else -> _list.value = ResourceState.Error("Data conversion fail")}
        }
    }

    private fun handleList(response: Response<CharacterModelResponse>): ResourceState<CharacterModelResponse> {
        if (response.isSuccessful) {
            response.body()?.let { values ->
                return ResourceState.Success(values)
            }
        }
        return ResourceState.Error(response.message())
    }
}