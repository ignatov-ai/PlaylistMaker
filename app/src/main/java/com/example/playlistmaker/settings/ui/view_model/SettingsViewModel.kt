package com.example.playlistmaker.settings.ui.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.R
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

    fun themeSwitcher(isChecked: Boolean) {
        changeState(isDarkTheme = isChecked)
    }

    fun shareBtnClick() {
        send.execute(text = getApplication<Application>().getString(R.string.practicumLink))
    }

    fun supportBtnClick() {
        sendTo.execute(
            email = arrayOf(getApplication<Application>().getString(R.string.mailto)),
            subject = getApplication<Application>().getString(R.string.mailTheme),
            text = getApplication<Application>().getString(R.string.mailMessage)
        )
    }

    fun licenseAgreementBtnClick() {
        view.execute(url = getApplication<Application>().getString(R.string.agreementLink))
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
            }
        }
    }
}
