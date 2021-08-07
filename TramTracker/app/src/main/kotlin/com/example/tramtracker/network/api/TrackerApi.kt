package com.example.tramtracker.network.api

import com.example.tramtracker.network.response.RouteResponse
import com.example.tramtracker.network.response.TokenResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TrackerApi {
    companion object {
        const val BASE_URL: String = "https://ws3.tramtracker.com.au/TramTracker/RestService/"
    }

    @GET("GetDeviceToken/?aid=TTIOSJSON&devInfo=HomeTime")
    suspend fun getToken(): Response<TokenResponse>

    @GET("GetNextPredictedRoutesCollection/{stopID}/{routeNo}/false/?aid=TTIOSJSON&cid=2")
    suspend fun getRoutes(
        @Path("stopID") stopID: String,
        @Path("routeNo") routeNo: String,
        @Query("tkn") token: String
    ): Response<RouteResponse>
}