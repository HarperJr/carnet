package com.harper.carnet.ui.settings.connection

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.harper.carnet.domain.diagnostics.ConnectionProvider
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables

class ConnectionSettingsViewModel(
    private val connectionProvider: ConnectionProvider
) : ViewModel() {
    val deviceIdentityLiveData: MutableLiveData<String> = MutableLiveData()
    val connectionErrorLiveData: MutableLiveData<String> = MutableLiveData()
    val connectionLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val progressLiveData: MutableLiveData<Boolean> = MutableLiveData()

    private var connectionDisposable: Disposable = Disposables.disposed()

    override fun onCleared() {
        connectionDisposable.dispose()
    }

    fun connect() {
        connectionDisposable = connectionProvider.provideConnection()
            .doOnSubscribe { progressLiveData.value = true }
            .subscribe({ deviceIdentity ->

            }, {

            })
    }
}