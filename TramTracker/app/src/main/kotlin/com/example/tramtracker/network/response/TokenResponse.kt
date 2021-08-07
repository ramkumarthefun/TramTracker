package com.example.tramtracker.network.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TokenResponse(
    @Json(name = "responseObject") val deviceTokens: List<DeviceToken>
)

@JsonClass(generateAdapter = true)
data class DeviceToken(
    @Json(name = "__type") val tokenType: String,
    @Json(name = "DeviceToken") val token: String
)