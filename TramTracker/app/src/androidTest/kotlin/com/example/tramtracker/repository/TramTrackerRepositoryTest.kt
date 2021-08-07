package com.example.tramtracker.repository

import androidx.test.filters.SmallTest
import com.example.tramtracker.network.api.TrackerApi
import com.example.tramtracker.network.response.RouteDetailResponse
import com.example.tramtracker.network.response.RouteResponse
import com.example.tramtracker.network.response.TokenResponse
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import java.util.*

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class TramTrackerRepositoryTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    private val trackerApi: TrackerApi = mock()
    private lateinit var trackerRepository: TrackerRepository

    @Before
    fun start() {
        hiltRule.inject()
        trackerRepository = TrackerRepository(trackerApi)
    }

    @Test
    fun testFetchToken() {
        runBlockingTest {
            val token = TokenResponse(emptyList())
            whenever(trackerApi.getToken()).thenReturn(Response.success(token))

            trackerRepository.fetchToken()

            verify(trackerApi).getToken()
        }
    }

    @Test
    fun testFetchRouteDetails() {
        runBlockingTest {
            val routes = RouteResponse(
                Collections.singletonList(
                    RouteDetailResponse(
                        "/Date(1628320710000+1000)/",
                        "North"
                    )
                )
            )
            whenever(
                trackerApi.getRoutes(
                    "4055",
                    "78",
                    "token"
                )
            ).thenReturn(Response.success(routes))

            trackerRepository.fetchRouteDetails("4055", "78", "token", "North")

            verify(trackerApi).getRoutes("4055", "78", "token")
        }
    }
}