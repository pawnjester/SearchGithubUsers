package com.example.github_ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.github_ui.databinding.FragmentDetailBinding
import com.example.github_ui.models.GithubUsersModel
import com.example.github_ui.utils.loadImage
import com.example.github_ui.utils.observe
import com.example.github_ui.viewmodel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private val userArgs: DetailFragmentArgs by navArgs()

    private val viewModel: DetailViewModel by viewModels()

    private var _binding: FragmentDetailBinding? = null

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setUserDetail(userArgs.user)

        observe(viewModel.user, ::init)


    }

    private fun init(user: GithubUsersModel?) {
        user?.let {
            binding.imgBackdrop.loadImage(it.avatarUrl)
            binding.userDetailName.text = it.login
        }
    }

}