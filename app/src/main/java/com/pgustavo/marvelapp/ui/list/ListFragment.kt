package com.pgustavo.marvelapp.ui.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pgustavo.marvelapp.R
import com.pgustavo.marvelapp.databinding.FragmentListBinding
import com.pgustavo.marvelapp.state.ResourceState
import com.pgustavo.marvelapp.ui.adapter.CharacterAdapter
import com.pgustavo.marvelapp.ui.base.BaseFragment
import com.pgustavo.marvelapp.util.hide
import com.pgustavo.marvelapp.util.show
import com.pgustavo.marvelapp.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ListFragment: BaseFragment<FragmentListBinding, ListViewModel>(){
    override val viewModel: ListViewModel by viewModels()
    private val characterAdapter by lazy { CharacterAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setRecyclerView()
        clickable()
        observers()
    }

    private fun observers() = lifecycleScope.launch {
        viewModel.list.collect{ resource ->
            when(resource){
                is ResourceState.Success -> {
                    resource.data?.let { values ->
                        binding.progressCircular.hide()
                        characterAdapter.characters = values.data.results.toList()
                    }
                }
                is ResourceState.Error-> {
                    binding.progressCircular.hide()
                    resource.message?.let { message ->
                        toast(getString(R.string.an_error_occurred))
                        Timber.tag("ListFragment").e("Error: $message")
                    }
                }
                is ResourceState.Loading -> {
                    binding.progressCircular.show()
                }
                else -> {}
            }
        }
    }

    private fun clickable() {
        characterAdapter.setOnClickListener { characterModel ->
            val action = ListFragmentDirections
                .actionListFragmentToDetailsFragment(characterModel)
            findNavController().navigate(action)

        }
    }

    private fun setRecyclerView() = with(binding) {
        rvCharacters.apply {
            adapter = characterAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item!!, requireView().findNavController())
        super.onOptionsItemSelected(item)
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentListBinding = FragmentListBinding.inflate(inflater, container, false)

}