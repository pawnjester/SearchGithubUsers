package com.example.github_ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.github_ui.adapters.UsersAdapter
import com.example.github_ui.databinding.FragmentSearchBinding
import com.example.github_ui.models.GithubUsersModel
import com.example.github_ui.utils.MarginItemDecoration
import com.example.github_ui.utils.observe
import com.example.github_ui.utils.show
import com.example.github_ui.viewmodel.LatestUiState
import com.example.github_ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    private var _binding: FragmentSearchBinding? = null

    private val binding get() = _binding!!

    @Inject
    lateinit var usersAdapter: UsersAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        binding.searchGithubEditText.doOnTextChanged { text, _, _, _ ->
            text?.let {
                if (it.length >= 3) {
                    viewModel.searchGithubUsers(it.toString())
                }
            }
        }

        usersAdapter.openUsersCallback = {
            val action = SearchFragmentDirections.actionSearchFragmentToDetailFragment(it)
            findNavController().navigate(action)
        }

        observe(viewModel.users, ::subscribeToUi)

    }

    private fun setupRecyclerView() {
        binding.rvGithubUsers.apply {
            layoutManager = LinearLayoutManager(
                requireContext(), RecyclerView.VERTICAL, false
            )
            addItemDecoration(MarginItemDecoration(16))
            adapter = usersAdapter
        }
    }

    private fun subscribeToUi(users: LatestUiState<List<GithubUsersModel>>?) {
        users?.let {
            when (it) {
                is LatestUiState.Loading -> {
                    binding.shimmerRecycler.show(true)
                    binding.shimmerRecycler.startShimmer()
                    binding.rvGithubUsers.show(false)
                }
                is LatestUiState.Success -> {
                    binding.shimmerRecycler.stopShimmer()
                    binding.shimmerRecycler.show(false)
                    binding.rvGithubUsers.show(true)
                    usersAdapter.setUsers(it.users)
                }
                else -> {

                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}