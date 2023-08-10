package com.boreneoux.githubuserssub1.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.boreneoux.githubuserssub1.MainApplication
import com.boreneoux.githubuserssub1.R
import com.boreneoux.githubuserssub1.databinding.ActivityMainBinding
import com.boreneoux.githubuserssub1.ui.setting.SettingActivity
import com.boreneoux.githubuserssub1.ui.setting.SettingViewModel
import com.boreneoux.githubuserssub1.utils.ViewModelFactory


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels(factoryProducer = {
        ViewModelFactory(MainApplication.injection)
    })
    private val settingViewModel: SettingViewModel by viewModels(factoryProducer = {
        ViewModelFactory(MainApplication.injection)
    })


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

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

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = binding.topAppBar.menu.findItem(R.id.searchUser).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()

                mainViewModel.findUser(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.favorite -> {
                    val intentToFavoriteUser =
                        Intent(this@MainActivity, FavoriteActivity::class.java)
                    this@MainActivity.startActivity(intentToFavoriteUser)
                    true
                }
                R.id.setting -> {
                    val intentToFavoriteUser =
                        Intent(this@MainActivity, SettingActivity::class.java)
                    this@MainActivity.startActivity(intentToFavoriteUser)
                    true
                }
                else -> {
                    false
                }
            }
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

        mainViewModel.user.observe(this) { items ->
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