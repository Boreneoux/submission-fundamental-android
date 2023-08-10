package com.boreneoux.githubuserssub1.ui.setting

import android.os.Bundle
import android.widget.CompoundButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.boreneoux.githubuserssub1.MainApplication
import com.boreneoux.githubuserssub1.databinding.ActivitySettingBinding
import com.boreneoux.githubuserssub1.utils.ViewModelFactory


class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private val settingViewModel: SettingViewModel by viewModels(factoryProducer = {
        ViewModelFactory(MainApplication.injection)
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val switchTheme = binding.switchTheme

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingViewModel.saveThemeSetting(isChecked)
        }
    }
}