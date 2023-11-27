package com.example.playlistmaker.settings.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.app.App
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.settings.ui.view_model.DarkThemeState
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.darkThemeStateLiveData.observe(viewLifecycleOwner) {
            render(it)
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
            DarkThemeState.DARK_THEME -> (requireActivity().application as App).switchTheme(true)
            DarkThemeState.LIGHT_THEME -> (requireActivity().application as App).switchTheme(false)
        }
    }
}