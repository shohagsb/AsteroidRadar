package com.udacity.asteroidradar.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        ViewModelProvider(
            this,
            MainViewModel.Factory(activity.application)
        ).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.asteroidRecycler.adapter = AsteroidAdapter()

        setHasOptionsMenu(true)

        viewModel.asteroids.observe(viewLifecycleOwner, {
            it?.let {
                //Log.d("MainFragmentTag", "onCreateView: ${it[0].absoluteMagnitude}")
                Log.d("MainFragmentTag", "onCreateView: ${it[0].closeApproachDate}")
                //Log.d("MainFragmentTag", "onCreateView: ${it[0].codename}")
                //Log.d("MainFragmentTag", "onCreateView: ${it[0].distanceFromEarth}")
                //Log.d("MainFragmentTag", "onCreateView: ${it[0].estimatedDiameter}")
            }
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
