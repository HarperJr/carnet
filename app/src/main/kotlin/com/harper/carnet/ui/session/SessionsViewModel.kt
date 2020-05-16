package com.harper.carnet.ui.session

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.harper.carnet.domain.model.LatLng
import com.harper.carnet.domain.model.Session
import com.harper.carnet.ext.rxLiveData
import io.reactivex.Observable
import java.util.*

class SessionsViewModel : ViewModel() {
    val activeSessionLiveData: LiveData<Session> = rxLiveData {
        Observable.just(Session.EMPTY)
    }

    val sessionsHistoryLiveData: LiveData<List<Session>> = rxLiveData {
        Observable.just(
            listOf(
                Session(
                    Date().apply { time -= 100000 },
                    Date().apply { time += 105353 },
                    LatLng(12.522525, 12.52516),
                    LatLng(14.152525, 12.636346)
                ),
                Session(
                    Date().apply { time -= 242500 },
                    Date().apply { time += 145353 },
                    LatLng(12.522525, 12.52516),
                    LatLng(14.152525, 12.636346)
                )
            )
        )
    }
}
