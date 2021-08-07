package com.example.tramtracker.repository

import androidx.test.filters.SmallTest
import com.example.tramtracker.di.TestNetworkModule.MOCK_WEB_SERVER_PORT
import com.example.tramtracker.network.api.TrackerApi
import com.example.tramtracker.util.MockResponseFileReader
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import java.util.*
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class TrackerRepositoryIntegrationTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var mockWebServer: MockWebServer

    @Inject
    @Named("test_retrofit")
    lateinit var retrofitBuilder: Retrofit.Builder

    private lateinit var trackerRepository: TrackerRepository
    private lateinit var trackerApi: TrackerApi

    @Before
    fun start() {
        hiltRule.inject()

        mockWebServer.start(MOCK_WEB_SERVER_PORT)
        trackerApi = retrofitBuilder
            .baseUrl(mockWebServer.url("/"))
            .build()
            .create(TrackerApi::class.java)

        trackerRepository = TrackerRepository(trackerApi)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun badTrackerRequest() = runBlocking {
        mockWebServer.apply {
            enqueue(
                MockResponse().setBody(
                    MockResponseFileReader("detail_error.json").content
                ).setResponseCode(502)
            )
        }

        val routeDetails = trackerRepository.fetchRouteDetails(
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString()
        )

        Truth.assertThat(routeDetails).isNull()
    }

    @Test
    fun goodTrackerRequest() = runBlocking {
        mockWebServer.apply {
            enqueue(
                MockResponse().setBody(
                    MockResponseFileReader("route_details.json").content
                )
            )
        }

        val routeDetails = trackerRepository.fetchRouteDetails(
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString()
        )

        Truth.assertThat(routeDetails).isNotNull()
    }

}