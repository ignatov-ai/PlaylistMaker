package com.example.playlistmaker.settings.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.app.App
import com.example.playlistmaker.app.DARK_THEME
import com.example.playlistmaker.R
import com.example.playlistmaker.app.THEME_PREFS
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.settings.ui.view_model.DarkThemeState
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory()
        )[SettingsViewModel::class.java]

        viewModel.darkThemeStateLiveData.observe(this) {
            render(it)
        }

        binding.backToMain.setOnClickListener {
            finish()
        }

        binding.themeSwitcher.isChecked = (viewModel.darkThemeStateLiveData.value == DarkThemeState.DARK_THEME)

        binding.themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            viewModel.themeSwitcher(checked)
        }

        binding.share.setOnClickListener {
            viewModel.shareBtnClick()
        }

        binding.support.setOnClickListener {
            viewModel.supportBtnClick()
        }

        binding.userAgreement.setOnClickListener {
            viewModel.licenseAgreementBtnClick()
        }
    }

    private fun render(state: DarkThemeState) {
        when (state) {
            DarkThemeState.DARK_THEME -> (application as App).switchTheme(true)
            DarkThemeState.LIGHT_THEME -> (application as App).switchTheme(false)
        }
    }
}