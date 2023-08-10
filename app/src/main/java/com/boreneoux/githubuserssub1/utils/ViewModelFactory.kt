package com.boreneoux.githubuserssub1.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.boreneoux.githubuserssub1.di.Injection
import com.boreneoux.githubuserssub1.ui.MainViewModel
import com.boreneoux.githubuserssub1.ui.setting.SettingPreferences
import com.boreneoux.githubuserssub1.ui.setting.SettingViewModel

class ViewModelFactory(private val injection: Injection) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(
                injection.database.favUserDao()
            )
            modelClass.isAssignableFrom(SettingViewModel::class.java) -> SettingViewModel(
                SettingPreferences.getInstance(injection.dataStore)
            )
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        } as T
    }
}