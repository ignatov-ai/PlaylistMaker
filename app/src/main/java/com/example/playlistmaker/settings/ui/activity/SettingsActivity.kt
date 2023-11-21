package com.example.playlistmaker.settings.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.app.App
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.settings.ui.view_model.DarkThemeState
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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