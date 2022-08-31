package com.pgustavo.marvelapp.ui.details

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.pgustavo.marvelapp.R
import com.pgustavo.marvelapp.data.model.entity.CharacterModel
import com.pgustavo.marvelapp.databinding.FragmentDetailsBinding
import com.pgustavo.marvelapp.ui.base.BaseFragment
import com.pgustavo.marvelapp.util.Image
import com.pgustavo.marvelapp.util.getImage
import com.pgustavo.marvelapp.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment: BaseFragment<FragmentDetailsBinding, DetailsViewModel>() {
    override val viewModel: DetailsViewModel by viewModels()

    private val args: DetailsFragmentArgs by navArgs()
    private lateinit var characterModel: CharacterModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        characterModel = args.character
        loadingCharacter(characterModel)
    }

    private fun loadingCharacter(characterModel: CharacterModel) = with(binding) {
        btnclose.setOnClickListener { activity?.onBackPressed() }
        tvNameCharacterDetails.text = characterModel.name
        if (characterModel.description.isEmpty()) {
            tvDescriptionCharacterDetails.text = requireContext().getString(R.string.text_description_empty)
        } else {
            tvDescriptionCharacterDetails.text = characterModel.description
        }
        getImage(imgCharacterDetails, characterModel.thumbnail.path, characterModel.thumbnail.extension)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_details, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> {
                viewModel.insert(characterModel)
                toast(getString(R.string.saved_successfully))
            }
            R.id.share -> {
                val imageView = binding.imgCharacterDetails
                Image.share(requireContext(), imageView)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDetailsBinding = FragmentDetailsBinding.inflate(inflater, container, false)
}