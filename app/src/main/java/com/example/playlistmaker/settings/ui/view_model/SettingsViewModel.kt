package com.example.playlistmaker.settings.ui.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker.creator.Creator
class SettingsViewModel(application: Application) : AndroidViewModel(application){
    private var mutableDarkThemeStateLiveData = MutableLiveData<DarkThemeState>()
    val darkThemeStateLiveData: LiveData<DarkThemeState> = mutableDarkThemeStateLiveData

    private val send = Creator.provideSendUseCase(getApplication<Application>())
    private val sendTo = Creator.provideSendToUseCase(getApplication<Application>())
    private val view = Creator.provideViewUseCase(getApplication<Application>())
    private val darkThemeInteractor = Creator.provideDarkThemeInteractor(getApplication<Application>())

    init {
        changeState(darkThemeState())
    }

    private fun changeState(isDarkTheme: Boolean) {
        if (isDarkTheme){
            mutableDarkThemeStateLiveData.value = DarkThemeState.DARK_THEME
        }
        else {
            mutableDarkThemeStateLiveData.value = DarkThemeState.LIGHT_THEME
        }
        darkThemeStateSave(isDarkTheme)
    }

    private fun darkThemeState() : Boolean{
        return darkThemeInteractor.darkThemeState()
    }

    private fun darkThemeStateSave(isDarkTheme: Boolean) {
        darkThemeInteractor.darkThemeStateSave(isDarkTheme)
    }
}
