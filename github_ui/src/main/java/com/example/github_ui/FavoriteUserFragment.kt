package com.example.github_ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.github_ui.adapters.FavoriteUserAdapter
import com.example.github_ui.databinding.FragmentFavoriteUserBinding
import com.example.github_ui.models.GithubUsersModel
import com.example.github_ui.utils.MarginItemDecoration
import com.example.github_ui.utils.observe
import com.example.github_ui.utils.show
import com.example.github_ui.viewmodel.LatestUiState
import com.example.github_ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteUserFragment : Fragment() {

    private var _binding: FragmentFavoriteUserBinding? = null

    private val binding get() = _binding!!

    @Inject
    lateinit var favoriteUserAdapter: FavoriteUserAdapter

    private val viewModel: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        viewModel.getFavoriteUsers()

        favoriteUserAdapter.openDetailsCallback = {
            val action =
                FavoriteUserFragmentDirections.actionFavoriteUserFragmentToNavigationFavorite(
                    it
                )
            findNavController().navigate(action)
        }

        favoriteUserAdapter.favoriteUserCallback = { model ->
            viewModel.favoriteUserCache(model)
        }

        observe(viewModel.favoriteUsers, ::subscribeToUi)

    }

    private fun setupRecyclerView() {
        binding.favoriteRv.layoutManager = LinearLayoutManager(
            requireContext(), RecyclerView.VERTICAL, false
        )
        binding.favoriteRv.addItemDecoration(MarginItemDecoration(16))
        binding.favoriteRv.adapter = favoriteUserAdapter
    }

    private fun subscribeToUi(items: LatestUiState<List<GithubUsersModel>>?) {
        items?.let {
            when (it) {
                is LatestUiState.Success -> {
                    binding.favoriteRv.show(true)
                    favoriteUserAdapter.setFavoriteUsersList(it.users)
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}