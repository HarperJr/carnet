package com.harper.carnet.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.harper.carnet.data.storage.SettingsStorage
import com.harper.carnet.ext.rxLiveData
import io.reactivex.Observable

class SettingsViewModel(private val settingsStorage: SettingsStorage) : ViewModel() {
    val notificationsCheckLiveData: LiveData<Boolean> =
        rxLiveData { Observable.just(settingsStorage.getNotificationsEnabled()) }
    val notificationSoundCheckLiveData: LiveData<Boolean> =
        rxLiveData { Observable.just(settingsStorage.getNotificationSoundEnabled()) }

    fun onNotificationCaseSwitched(isChecked: Boolean) {
        settingsStorage.setNotificationsEnabled(isChecked)
    }

    fun onNotificationSoundCaseSwitched(isChecked: Boolean) {
        settingsStorage.setNotificationSoundEnabled(isChecked)
    }
}