package com.harper.carnet.ui.settings.connection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.harper.carnet.data.storage.AppStorage
import com.harper.carnet.domain.diagnostics.DiagnosticsProvider
import com.harper.carnet.ext.rxLiveData
import io.ktor.util.KtorExperimentalAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import timber.log.Timber

class ConnectionSettingsViewModel(
    private val diagnosticsProvider: DiagnosticsProvider,
    private val appStorage: AppStorage
) : ViewModel() {
    val deviceIdentityLiveData: LiveData<String> = rxLiveData {
        diagnosticsProvider.isConnected().map {
            appStorage.getDeviceIdentity()
        }
    }

    val connectionLiveData: LiveData<Boolean> = rxLiveData {
        diagnosticsProvider.isConnected()
    }

    val progressLiveData: MutableLiveData<Boolean> = MutableLiveData(false)
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    private var connectionDisposable: Disposable = Disposables.disposed()

    override fun onCleared() {
        connectionDisposable.dispose()
    }

    @KtorExperimentalAPI
    fun connect(deviceIdentity: String) {
        connectionDisposable = diagnosticsProvider.connect(deviceIdentity)
            .doOnSuccess { appStorage.setDeviceIdentity(it) }
            .doOnSubscribe { progressLiveData.value = true }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                progressLiveData.value = false
            }, {
                progressLiveData.value = false
                Timber.e(it)
            })
    }
}