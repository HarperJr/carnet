package com.harper.carnet.ui.session

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.harper.carnet.domain.model.*
import com.harper.carnet.domain.session.SessionProvider
import com.harper.carnet.ext.rxLiveData
import io.reactivex.Observable
import java.util.*

class SessionsViewModel(private val sessionProvider: SessionProvider) : ViewModel() {
    val activeSessionLiveData: LiveData<Session> = rxLiveData { sessionProvider.provideActiveSession() }

    val sessionsHistoryLiveData: LiveData<List<Session>> = rxLiveData {
        Observable.just(
            listOf(
                Session(
                    0,
                    Date().apply { time -= 100000 },
                    Date().apply { time += 105353 },
                    false,
                    Location(LatLng(56.135515, 40.357844), "Pr. Stroiteley d 12"),
                    Location(LatLng(56.140486, 40.395699), "Ul. Gorkogo d 42/1"),
                    listOf(
                        DiagnosticValue(ValueType.VOLTAGE, 12.2),
                        DiagnosticValue(ValueType.FUEL_LEVEL, 0.25),
                        DiagnosticValue(ValueType.SPEED, 72)
                    ),
                    emptyList()
                ),
                Session(
                    1,
                    Date().apply { time -= 242500 },
                    Date().apply { time += 145353 },
                    false,
                    Location(LatLng(56.130007, 40.405131), "Oktyabrskiy rayon"),
                    Location(LatLng(56.119522, 40.363095), "Pr. Lenina d. 30-32"),
                    listOf(
                        DiagnosticValue(ValueType.VOLTAGE, 11.2),
                        DiagnosticValue(ValueType.FUEL_LEVEL, 0.12),
                        DiagnosticValue(ValueType.SPEED, 44)
                    ),
                    emptyList()
                )
            )
        )
    }
}