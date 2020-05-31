package com.harper.carnet.domain.session

import com.harper.carnet.data.repository.SessionRepository
import com.harper.carnet.domain.model.Session
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SessionProvider(private val sessionRepository: SessionRepository) {

    fun provideActiveSession(): Observable<Session> {
        return Observable.fromCallable { sessionRepository.findActiveSession() ?: Session.EMPTY }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}