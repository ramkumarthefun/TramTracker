package com.example.tramtracker.network.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RouteResponse(
    @Json(name = "responseObject") val routeDetails: List<RouteDetailResponse>?
)

@JsonClass(generateAdapter = true)
data class RouteDetailResponse(
    @Json(name = "PredictedArrivalDateTime") val predictedArrivalDateTime: String?,
    @Json(name = "Destination") val destination: String?
)