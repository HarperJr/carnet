package com.harper.carnet.domain.session

import com.harper.carnet.data.repository.SessionRepository
import com.harper.carnet.domain.model.Location
import com.harper.carnet.domain.model.Place
import com.harper.carnet.domain.model.Session
import io.reactivex.Observable
import java.util.*

class SessionManager(private val sessionRepository: SessionRepository) {

    fun createSession(originPlace: Place, destPlace: Place): Observable<Boolean> {
        return Observable.create { sub ->
            val isSessionExists = sessionRepository.findActiveSession() != null
            if (isSessionExists) {
                sub.onNext(false)
            } else {
                sessionRepository.insert(
                    Session(
                        0,
                        Date(),
                        null,
                        true,
                        Location(originPlace.location, originPlace.name),
                        Location(destPlace.location, destPlace.name)
                    )
                )
                sub.onNext(true)
            }
        }
    }
}
