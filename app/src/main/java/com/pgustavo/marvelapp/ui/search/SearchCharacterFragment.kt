package com.pgustavo.marvelapp.ui.search

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pgustavo.marvelapp.R
import com.pgustavo.marvelapp.databinding.FragmentSearchCharacterBinding
import com.pgustavo.marvelapp.state.ResourceState
import com.pgustavo.marvelapp.ui.adapter.CharacterAdapter
import com.pgustavo.marvelapp.ui.base.BaseFragment
import com.pgustavo.marvelapp.util.Constants.DEFAULT_SEARCH
import com.pgustavo.marvelapp.util.Constants.LAST_SEARCH
import com.pgustavo.marvelapp.util.hide
import com.pgustavo.marvelapp.util.show
import com.pgustavo.marvelapp.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class SearchCharacterFragment: BaseFragment<FragmentSearchCharacterBinding, SearchCharacterViewModel>() {
    override val viewModel: SearchCharacterViewModel by viewModels()
    private val characterAdapter by lazy { CharacterAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        clickable()
        val lastSearch = savedInstanceState?.getString(LAST_SEARCH) ?: DEFAULT_SEARCH
        searchInit(lastSearch)
        observers()
    }

    private fun observers() = lifecycleScope.launch {
        viewModel.searchCharacter.collect { result ->
            when (result) {
                is ResourceState.Success -> {
                    binding.progressbarSearch.hide()
                    result.data?.let {
                        characterAdapter.characters = it.data.results.toList()
                    }
                }
                is ResourceState.Error -> {
                    binding.progressbarSearch.hide()
                    result.message?.let { message ->
                        toast(getString(R.string.an_error_occurred))
                        Timber.tag("SearchCharacterFragment").e("Error: $message")
                    }
                }
                is ResourceState.Loading -> {
                    binding.progressbarSearch.show()
                }
                else -> {}
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_SEARCH,
        binding.edSearchCharacter.editableText.trim().toString())
    }

    private fun searchInit(last_search: String) = with(binding) {
        edSearchCharacter.setText(last_search)
        edSearchCharacter.setOnEditorActionListener{_, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO){
                updateList()
                true
            }else {
                false
            }
        }
        edSearchCharacter.setOnKeyListener{_, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateList()
                true
            } else {
                false
            }
        }
        btngo.setOnClickListener {
            updateList()
        }
    }



    private fun updateList() = with(binding) {
        edSearchCharacter.editableText.trim().let {
            if (it.isNotEmpty()){
                search(it.toString())
            }
        }
    }

    private fun search(search: String) {
        viewModel.fetch(search)
    }

    private fun clickable() {
        characterAdapter.setOnClickListener { characterModel ->
            val action = SearchCharacterFragmentDirections
                .actionSearchCharacterFragmentToDetailsFragment(characterModel)
            findNavController().navigate(action)
        }
    }

    private fun setRecyclerView() = with(binding) {
        rvSearchCharacter.apply {
            adapter = characterAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchCharacterBinding =
        FragmentSearchCharacterBinding.inflate(inflater, container,false)
}