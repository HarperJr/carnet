package com.harper.carnet.ui.session

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.harper.carnet.domain.model.*
import com.harper.carnet.ext.rxLiveData
import io.reactivex.Observable
import java.util.*

class SessionsViewModel : ViewModel() {
    val activeSessionLiveData: LiveData<Session> = rxLiveData {
        Observable.just(
            Session(
                0,
                Date().apply { time -= 100000 },
                Date().apply { time += 105353 },
                Location(LatLng(56.135515, 40.357844), "Pr. Stroiteley d 12"),
                Location(LatLng(56.140486, 40.395699), "Ul. Gorkogo d 42/1"),
                listOf(Value(ValueType.VOLTAGE, 12.8), Value(ValueType.FUEL_LEVEL, 0.55), Value(ValueType.SPEED, 56)),
                listOf(Notification(NotificationType.TRAFFIC_CONGESTION, Date().apply { time -= 60000 },  Location(LatLng(56.140486, 40.395699), "Ul. Gorkogo d 42/1")))
            )
        )
    }

    val sessionsHistoryLiveData: LiveData<List<Session>> = rxLiveData {
        Observable.just(
            listOf(
                Session(
                    0,
                    Date().apply { time -= 100000 },
                    Date().apply { time += 105353 },
                    Location(LatLng(56.135515, 40.357844), "Pr. Stroiteley d 12"),
                    Location(LatLng(56.140486, 40.395699), "Ul. Gorkogo d 42/1"),
                    listOf(Value(ValueType.VOLTAGE, 12.2), Value(ValueType.FUEL_LEVEL, 0.25), Value(ValueType.SPEED, 72)),
                    emptyList()
                ),
                Session(
                    1,
                    Date().apply { time -= 242500 },
                    Date().apply { time += 145353 },
                    Location(LatLng(56.130007, 40.405131), "Oktbrsiy rayon"),
                    Location(LatLng(56.119522, 40.363095), "Pr. Lenina d. 30-32"),
                    listOf(Value(ValueType.VOLTAGE, 11.2), Value(ValueType.FUEL_LEVEL, 0.12), Value(ValueType.SPEED, 44)),
                    emptyList()
                )
            )
        )
    }
}