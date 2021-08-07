package com.example.tramtracker.viewmodel

import androidx.test.filters.SmallTest
import com.example.tramtracker.repository.TrackerRepository
import com.example.tramtracker.ui.viewmodel.TrackerViewModel
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class TrackerViewModelTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    private val trackerRepository: TrackerRepository = mock()
    private lateinit var viewModel: TrackerViewModel

    @Before
    fun start() {
        hiltRule.inject()
        viewModel = TrackerViewModel(trackerRepository)
    }

    @Test
    fun verifyGetRouteDetails() {
        runBlockingTest {
            viewModel.getRouteDetails(Dispatchers.Unconfined)
            verify(trackerRepository).fetchRouteDetails(eq("4055"), eq("78"), any(), eq("North"))
        }
    }

}