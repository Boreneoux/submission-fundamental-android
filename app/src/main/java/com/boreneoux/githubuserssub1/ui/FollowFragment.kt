package com.boreneoux.githubuserssub1.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.boreneoux.githubuserssub1.MainApplication
import com.boreneoux.githubuserssub1.databinding.FragmentFollowBinding
import com.boreneoux.githubuserssub1.utils.ViewModelFactory

class FollowFragment : Fragment() {
    private val mainViewModel: MainViewModel by viewModels(factoryProducer = {
        ViewModelFactory(MainApplication.injection)
    })

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var position = arguments?.getInt(ARG_SECTION_NUMBER)
        var username = arguments?.getString(ARG_USERNAME)

        val adapter = UserAdapter(onClick = {
            val intentToDetailUser =
                Intent(requireContext(), DetailUserActivity::class.java)
            intentToDetailUser.putExtra(DetailUserActivity.EXTRA_LOGIN, it)
            requireContext().startActivity(intentToDetailUser)
        })
        binding.rvFollow.adapter = adapter

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollow.layoutManager = layoutManager

        mainViewModel.userFollowers(username!!)
        mainViewModel.userFollowing(username)

        mainViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        arguments?.let {
            position = it.getInt(ARG_SECTION_NUMBER)
            username = it.getString(ARG_USERNAME)
        }

        if (position == 1) {
            mainViewModel.userFollowers.observe(viewLifecycleOwner) { items ->
                adapter.submitList(items)
            }
        } else {
            mainViewModel.userFollowing.observe(viewLifecycleOwner) { items ->
                adapter.submitList(items)
            }
        }
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val ARG_SECTION_NUMBER = "position"
        const val ARG_USERNAME = "username"
    }

}