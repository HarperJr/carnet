package com.harper.carnet.ext

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import timber.log.Timber

inline fun <reified T> Any.cast(): T =
    this as? T ?: throw RuntimeException("Unable to cast ${this::class} to ${T::class}")

fun <T> rxLiveData(rxFlow: () -> Observable<T>): LiveData<T> = object : LiveData<T>() {
    private var disposable: Disposable = Disposables.disposed()

    override fun onActive() {
        disposable = rxFlow.invoke()
            .subscribe({ this.postValue(it) }, { Timber.e(it) })
    }

    override fun onInactive() {
        disposable.dispose()
    }
}

fun <T> LiveData<T>.observe(fragment: Fragment, observer: (T) -> Unit) = this.observe({ fragment.lifecycle }, observer)

fun <T> Observable<T>.subscribeLiveData(liveData: MutableLiveData<T>) = this.subscribe { liveData.postValue(it) }