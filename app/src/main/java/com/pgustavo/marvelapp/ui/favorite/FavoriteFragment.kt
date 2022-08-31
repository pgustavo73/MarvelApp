package com.pgustavo.marvelapp.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pgustavo.marvelapp.R
import com.pgustavo.marvelapp.databinding.FragmentFavoriteBinding
import com.pgustavo.marvelapp.state.ResourceState
import com.pgustavo.marvelapp.ui.adapter.CharacterAdapter
import com.pgustavo.marvelapp.ui.base.BaseFragment
import com.pgustavo.marvelapp.util.hide
import com.pgustavo.marvelapp.util.show
import com.pgustavo.marvelapp.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteFragment: BaseFragment<FragmentFavoriteBinding, FavoriteViewModel>() {
    override val viewModel: FavoriteViewModel by viewModels()
    private val characterAdapter by lazy { CharacterAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        clickable()
        observers()
    }

    private fun observers() = lifecycleScope.launch {
        viewModel.favorites.collect { resource ->
            when (resource) {
                is ResourceState.Success -> {
                    resource.data?.let {
                        binding.tvEmptyList.hide()
                        characterAdapter.characters = it.toList()
                    }
                }
                is ResourceState.Empty -> {
                    binding.tvEmptyList.show()
                }
                else -> {}
            }
        }
    }

    private fun clickable() {
        characterAdapter.setOnClickListener { characterModel ->
            val action = FavoriteFragmentDirections
                .actionFavoriteFragmentToDetailsFragment(characterModel)
            findNavController().navigate(action)
        }
    }

    private fun setRecyclerView() = with(binding) {
        rvFavoriteCharacter.apply {
            adapter = characterAdapter
            layoutManager = LinearLayoutManager(context)
        }
        ItemTouchHelper(swipeHelperCallback()).attachToRecyclerView(rvFavoriteCharacter)
    }

    private fun swipeHelperCallback(): ItemTouchHelper.SimpleCallback {
        return object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or
        ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerview: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val character = characterAdapter.getCharacterPosition(viewHolder.adapterPosition)
                viewModel.delete(character).also {
                    toast(getString(R.string.message_delete_character))
                }
            }
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoriteBinding = FragmentFavoriteBinding.inflate(inflater, container, false)
}