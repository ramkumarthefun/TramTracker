package com.example.tramtracker.ui.viewmodel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tramtracker.repository.TrackerRepository
import com.example.tramtracker.repository.model.Token
import com.example.tramtracker.util.TAG
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class TrackerViewModel
@ViewModelInject
constructor(
    private val trackerRepository: TrackerRepository
) : ViewModel() {
    private var routeDetailsList: MutableList<String>? = null
    private var token: Token? = null
    val routeDetailsLiveData: MutableLiveData<MutableList<String>> = MutableLiveData()

    /*
        Fetches token first which is used to get route details. Both North and South bound tram details are fetched
        and published in onSuccess
     */
    fun getRouteDetails(dispatcher: CoroutineDispatcher, isRefresh: Boolean) {
        viewModelScope.launch(dispatcher) {
            val request = kotlin.runCatching {
                if (isRefresh || (routeDetailsList == null || routeDetailsList?.size == 0)) {
                    if (token == null) {
                        token = trackerRepository.fetchToken()
                    }

                    token?.deviceToken?.let { tkn ->
                        trackerRepository.fetchRouteDetails(
                            "4055",
                            "78",
                            tkn,
                            "North"
                        )
                            ?.let {
                                routeDetailsList =
                                    it.routeDetailsList.filterNotNull().toMutableList()
                            }
                        trackerRepository.fetchRouteDetails(
                            "4155",
                            "78",
                            tkn,
                            "South"
                        )
                            ?.let {
                                routeDetailsList?.addAll(
                                    it.routeDetailsList.filterNotNull().toMutableList()
                                )
                            }
                    }
                }
            }
            request.onSuccess {
                routeDetailsLiveData.postValue(routeDetailsList)
            }
            request.onFailure { e: Throwable ->
                Log.e(TAG, "Error fetching route details", e)
            }
        }
    }
}