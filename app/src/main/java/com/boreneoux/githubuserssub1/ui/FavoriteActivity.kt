package com.boreneoux.githubuserssub1.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.boreneoux.githubuserssub1.MainApplication
import com.boreneoux.githubuserssub1.data.local.entity.FavUserEntity
import com.boreneoux.githubuserssub1.data.model.GithubResponse
import com.boreneoux.githubuserssub1.databinding.ActivityFavoriteBinding
import com.boreneoux.githubuserssub1.utils.ViewModelFactory

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private val mainViewModel: MainViewModel by viewModels(factoryProducer = {
        ViewModelFactory(MainApplication.injection)
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = UserAdapter(onClick = {
            val intentToDetailUser =
                Intent(this, DetailUserActivity::class.java)
            intentToDetailUser.putExtra(DetailUserActivity.EXTRA_LOGIN, it)
            this.startActivity(intentToDetailUser)
        })
        binding.rvUsers.adapter = adapter

        val layoutManager = LinearLayoutManager(this)
        binding.rvUsers.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUsers.addItemDecoration(itemDecoration)

        mainViewModel.userFav.observe(this) { users: List<FavUserEntity> ->
            val items = arrayListOf<GithubResponse>()
            users.map {
                val item = GithubResponse(login = it.username, avatarUrl = it.avatarUrl)
                items.add(item)
            }
            adapter.submitList(items)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}