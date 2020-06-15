package com.harper.carnet.ui.session

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.harper.carnet.domain.diagnostics.ValuesProvider
import com.harper.carnet.domain.model.DiagnosticValue
import com.harper.carnet.domain.model.Session
import com.harper.carnet.domain.session.SessionProvider
import com.harper.carnet.ext.rxLiveData

class SessionsViewModel(
    private val sessionProvider: SessionProvider,
    private val valuesProvider: ValuesProvider
    ) : ViewModel() {
    val activeSessionLiveData: LiveData<Session> = rxLiveData { sessionProvider.provideActiveSession() }
    val activeSessionDiagnosticsProvider: LiveData<List<DiagnosticValue<*>>> = rxLiveData { valuesProvider.provideValues() }
    val sessionsHistoryLiveData: LiveData<List<Session>> = rxLiveData { sessionProvider.provideSessionHistory() }
}