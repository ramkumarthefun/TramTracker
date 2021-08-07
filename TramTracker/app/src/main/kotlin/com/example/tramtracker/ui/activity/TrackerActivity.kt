package com.example.tramtracker.ui.activity

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tramtracker.R
import com.example.tramtracker.databinding.ActivityTramTrackerBinding
import com.example.tramtracker.ui.adapter.RouteDetailsAdapter
import com.example.tramtracker.ui.viewmodel.TrackerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@AndroidEntryPoint
class TrackerActivity : AppCompatActivity() {
    private val viewModel: TrackerViewModel by viewModels()

    @Inject
    lateinit var routeDetailsAdapter: RouteDetailsAdapter

    private lateinit var binding: ActivityTramTrackerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_MaterialComponents_Light_NoActionBar)
        super.onCreate(savedInstanceState)
        binding = ActivityTramTrackerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()

        //recycler view and progressbar is made visible when load button is clicked
        binding.load.setOnClickListener {
            binding.tramTrackerRecyclerView.visibility = VISIBLE
            binding.progressCircular.visibility = VISIBLE
            viewModel.getRouteDetails(Dispatchers.IO)
        }

        //recycler view and progressbar is hidden when load button is clicked
        binding.clear.setOnClickListener {
            binding.tramTrackerRecyclerView.visibility = GONE
            binding.progressCircular.visibility = GONE
        }

        //progressbar is visible when Tracker screen is visible
        binding.progressCircular.visibility = VISIBLE

        viewModel.routeDetailsLiveData.observe(this, { routeDetails: MutableList<String> ->
            //progressbar is hidden when recycler view is to be loaded with arrival times
            binding.progressCircular.visibility = GONE
            if (routeDetails.isNullOrEmpty().not()) {
                routeDetailsAdapter.submitList(routeDetails)
            }
        })

        viewModel.getRouteDetails(Dispatchers.IO)
    }

    private fun initRecyclerView() {
        val recyclerView: RecyclerView = binding.tramTrackerRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = routeDetailsAdapter
    }
}