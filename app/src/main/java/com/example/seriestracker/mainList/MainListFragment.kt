package com.example.seriestracker.mainList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.seriestracker.R
import com.example.seriestracker.adapters.*
import com.example.seriestracker.database.SeriesListEntityDatabase
import com.example.seriestracker.databinding.FragmentMainListBinding
import com.google.android.material.snackbar.Snackbar

class MainListFragment : Fragment() {

    lateinit var viewModel: MainListViewModel

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        val binding: FragmentMainListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main_list, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = SeriesListEntityDatabase.getInstance(application).seriesListEntityDatabaseDao
        val mainListViewModelFactory = MainListViewModelFactory( dataSource, application )
        val mainListViewModel = ViewModelProviders.of( this, mainListViewModelFactory).get(
            MainListViewModel::class.java)

        binding.mainListViewModel = mainListViewModel

        val defaultSpanCount = 1
        val manager = GridLayoutManager(activity, defaultSpanCount)
        manager.spanSizeLookup = object: GridLayoutManager.SpanSizeLookup()
        {
            override fun getSpanSize(position: Int) = defaultSpanCount
        }
        binding.entityList.layoutManager = manager


        val adapter = SeriesListEntityAdapter (
            SeriesListEntityCheckmarkListener
            { entityId -> mainListViewModel.onClickedCheckmark( entityId ) },

            SeriesListEntityTextAreaListener
            { entityId -> mainListViewModel.onClickedText( entityId ) },

            SeriesListEntitySeasonListener
            { entityId -> mainListViewModel.onClickedSeason( entityId ) },

            SeriesListEntityEpisodeListener
            { entityId -> mainListViewModel.onClickedEpisode( entityId ) },

            SeriesListHeaderListener
            { mainListViewModel.onClickedHeader() }
        )
        binding.entityList.adapter = adapter

        viewModel = mainListViewModel

        // Observers
        observeMainListEntities(adapter)
        observeShowSnackbarEvent()
        observeNavigateToAddNewEntity()
        observeNavigateToEditEntity()
        return binding.root
    }

    private fun observeMainListEntities(adapter: SeriesListEntityAdapter) {
        viewModel.entities.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.addHeaderAndSubmitList(it)
            }
        })
    }

    private fun observeNavigateToAddNewEntity(){
        viewModel.navigateToAddNewEntity.observe(this, Observer {
            if (it == true)
            {
                this.findNavController().navigate(
                    MainListFragmentDirections.actionMainFragmentToAddSeriesListEntityFragment()
                )

                // Make sure to only navigate once, even if the device has a configuration change
                viewModel.doneNavigatingToAddNewEntity()
            }
        })
    }

    private fun observeNavigateToEditEntity(){
        viewModel.navigateToEditEntity.observe(this, Observer { entityId ->
            if (entityId != -1L)
            {
                if (this.findNavController().currentDestination?.id == R.id.mainListFragment){
                    this.findNavController().navigate(
                        MainListFragmentDirections.actionMainListFragmentToEditSeriesListEntityFragment(entityId)
                    )
                }

                // Make sure to only navigate once, even if the device has a configuration change
                viewModel.doneNavigatingToEditEntity()
            }
        })
    }

    private fun observeShowSnackbarEvent(){
        viewModel.showSnackbarEvent.observe(this, Observer {
            if (it != "") {
                Snackbar.make(
                    activity!!.findViewById(android.R.id.content),
                    "$it",
                    Snackbar.LENGTH_SHORT
                ).show()
                viewModel.doneShowingSnackbar()
            }
        })
    }
}