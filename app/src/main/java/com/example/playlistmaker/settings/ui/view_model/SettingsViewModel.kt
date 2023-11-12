package com.example.playlistmaker.settings.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.api.DarkThemeInteractor
import com.example.playlistmaker.sharing.domain.impl.SendToUseCase
import com.example.playlistmaker.sharing.domain.impl.SendUseCase
import com.example.playlistmaker.sharing.domain.impl.ViewUseCase

class SettingsViewModel(
    private val darkThemeInteractor: DarkThemeInteractor,
    private val send: SendUseCase,
    private val sendTo: SendToUseCase,
    private val view: ViewUseCase) : ViewModel(){

    companion object {
        private const val PRACTICUM_LINK = "Пройди курс разработчика Android приложений от Яндекс. Ссылка: https://practicum.yandex.ru/android-developer/"
        private const val MAILTO = "ignatov-ai@yandex.ru"
        private const val MAIL_THEME = "Сообщение разработчикам и разработчицам приложения Playlist Maker"
        private const val MAIL_MESSAGE = "Спасибо разработчикам и разработчицам за крутое приложение!"
        private const val AGREEMENT_LINK = "https://yandex.ru/legal/practicum_offer/"
    }

    private var mutableDarkThemeStateLiveData = MutableLiveData<DarkThemeState>()
    val darkThemeStateLiveData: LiveData<DarkThemeState> = mutableDarkThemeStateLiveData

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
        send.execute(text = PRACTICUM_LINK)
    }

    fun supportBtnClick() {
        sendTo.execute(
            email = arrayOf(MAILTO),
            subject = MAIL_THEME,
            text = MAIL_MESSAGE
        )
    }

    fun licenseAgreementBtnClick() {
        view.execute(url = AGREEMENT_LINK)
    }
}