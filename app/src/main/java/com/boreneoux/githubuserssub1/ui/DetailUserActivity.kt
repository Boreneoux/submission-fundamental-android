package com.boreneoux.githubuserssub1.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.boreneoux.githubuserssub1.MainApplication
import com.boreneoux.githubuserssub1.R
import com.boreneoux.githubuserssub1.data.local.entity.FavUserEntity
import com.boreneoux.githubuserssub1.data.model.DetailUserResponse
import com.boreneoux.githubuserssub1.databinding.ActivityDetailUserBinding
import com.boreneoux.githubuserssub1.utils.ViewModelFactory
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private val mainViewModel: MainViewModel by viewModels(factoryProducer = {
        ViewModelFactory(MainApplication.injection)
    })

    private var favFlag = false
    private lateinit var favUserData: FavUserEntity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val login = intent.getStringExtra(EXTRA_LOGIN)

        if (login != null) {
            mainViewModel.detailUser(login)
            mainViewModel.isUserFav(login)
        }

        mainViewModel.toastVal.observe(this) { state ->
            state?.let {
                if (state) {
                    Toast.makeText(this, "Retrieved the data", Toast.LENGTH_SHORT).show()
                    mainViewModel.toastShown()
                } else {
                    Toast.makeText(this, "Retrieve data failed", Toast.LENGTH_SHORT).show()
                    mainViewModel.toastShown()
                }
            }
        }

        mainViewModel.userDetail.observe(this) { item ->
            setDetailUser(item)
            favUserData = FavUserEntity(item.login ?: "", item.avatarUrl)
        }

        mainViewModel.isUserFav.observe(this) {
            favFlag = it
            changeFav(it)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = login.toString()
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f

        setupFavUserFab()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setDetailUser(item: DetailUserResponse) {
        binding.tvUsername.text = item.login
        binding.tvName.text = item.name
        binding.tvFollower.text = item.followers.toString()
        binding.tvFollowing.text = item.following.toString()
        Glide.with(this)
            .load(item.avatarUrl)
            .into(binding.ivProfile)
    }

    private fun setupFavUserFab() {
        binding.fabFavorite.setOnClickListener {
            if (favFlag) {
                mainViewModel.delete(favUserData)
                favFlag = false
                Toast.makeText(applicationContext, "Deleted from Favorite", Toast.LENGTH_SHORT)
                    .show()
            } else {
                mainViewModel.insert(favUserData)
                favFlag = true
                Toast.makeText(applicationContext, "Added to Favorite", Toast.LENGTH_SHORT).show()

            }
            changeFav(favFlag)
        }
    }

    private fun changeFav(state: Boolean) {
        if (state) {
            binding.fabFavorite.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_favorite_fill
                )
            )
        } else {
            binding.fabFavorite.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_favorite
                )
            )
        }
    }

    companion object {
        const val EXTRA_LOGIN = "extra_login"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}