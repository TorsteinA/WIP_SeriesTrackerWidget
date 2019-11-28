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
import com.example.seriestracker.database.SeriesListEntityDatabase
import com.example.seriestracker.databinding.FragmentMainListBinding
import com.example.seriestracker.adapters.SeriesListEntityAdapter
import com.example.seriestracker.adapters.SeriesListEntityListener
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.header.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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


        val adapter = SeriesListEntityAdapter ( SeriesListEntityListener
            { entityId ->
                    mainListViewModel.onClickedEntity ( entityId )
            } )
        binding.entityList.adapter = adapter

        viewModel = mainListViewModel

        // Observers
        mainListViewModel.entities.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.addHeaderAndSubmitList(it)
            }
        })

        mainListViewModel.showSnackbarEvent.observe(this, Observer {
            if (it != -1L) {
                Snackbar.make(
                    activity!!.findViewById(android.R.id.content),
                    "Clicked Entity with id: $it",
                    Snackbar.LENGTH_SHORT
                ).show()
                mainListViewModel.doneShowingSnackbar()
            }
        })

        mainListViewModel.navigateToAddNewEntity.observe(this, Observer {
            if (it == true)
            {
                this.findNavController().navigate(
                    MainListFragmentDirections.actionMainFragmentToAddSeriesListEntityFragment()
                )

                // Make sure to only navigate once, even if the device has a configuration change
                mainListViewModel.doneNavigatingToAddNewEntity()
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        list_header.setOnClickListener {
//            run {
//                this.findNavController().navigate(
//                    MainListFragmentDirections.actionMainFragmentToAddSeriesListEntityFragment()
//                )
//
//                // Make sure to only navigate once, even if the device has a configuration change
//                viewModel.doneNavigatingToAddNewEntity()
//            }
//        }
    }
}