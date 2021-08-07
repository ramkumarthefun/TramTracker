package com.example.tramtracker.repository

import android.util.Log
import com.example.tramtracker.network.api.TrackerApi
import com.example.tramtracker.network.response.RouteResponse
import com.example.tramtracker.network.response.TokenResponse
import com.example.tramtracker.repository.model.RouteDetails
import com.example.tramtracker.repository.model.Token
import com.example.tramtracker.util.TAG
import retrofit2.Response
import java.util.*

open class TrackerRepository(private val trackerApi: TrackerApi) {

    suspend fun fetchToken(): Token? {
        val request = kotlin.runCatching {
            trackerApi.getToken()
        }
        request.onSuccess { response: Response<TokenResponse> ->
            val body: TokenResponse? = response.body()
            if (response.isSuccessful && body != null) {
                return Token(body.deviceTokens.firstOrNull()?.token)
            }
        }
        request.onFailure { e: Throwable ->
            Log.e(TAG, "Error fetching token", e)
        }

        return null
    }

    suspend fun fetchRouteDetails(
        stopID: String,
        routeNo: String,
        token: String,
        direction: String
    ): RouteDetails? {
        val request = kotlin.runCatching {
            trackerApi.getRoutes(stopID, routeNo, token)
        }
        request.onSuccess { response: Response<RouteResponse> ->
            val body: RouteResponse? = response.body()
            if (response.isSuccessful && body != null && body.routeDetails != null) {
                //direction, routeNo and destination is shown as header title
                val list =
                    mutableListOf(direction + " Route: " + routeNo + " Destination: " + body.routeDetails.first().destination)
                list.addAll(body.routeDetails.mapNotNull { it.predictedArrivalDateTime }
                    .map { dateFromDotNetDate(it).split(" ")[3].substring(0, 5) })
                return RouteDetails(
                    list
                )
            }
        }
        request.onFailure { e: Throwable ->
            Log.e(TAG, "Error fetching route details", e)
        }

        return null
    }

    private fun dateFromDotNetDate(dotNetDate: String): String {
        val startIndex = dotNetDate.indexOf("(") + 1
        val endIndex = dotNetDate.indexOf("+")
        val date = dotNetDate.substring(startIndex, endIndex)
        val unixTime = date.toLong()
        return Date(unixTime).toString()
    }
}