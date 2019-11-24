package com.example.seriestracker.addSeriesListEntity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.seriestracker.R
import com.example.seriestracker.database.SeriesListEntityDatabase
import com.example.seriestracker.databinding.FragmentAddNewSeriesListEntityBinding
import com.example.seriestracker.hideKeyboard

class AddSeriesListEntityFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentAddNewSeriesListEntityBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_add_new_series_list_entity, container, false)
        val application = requireNotNull(this.activity).application

        //val arguments = AddSeriesListEntityFragmentArgs.fromBundle(arguments!!)

        val dataSource = SeriesListEntityDatabase.getInstance(application).seriesListEntityDatabaseDao
        val viewModelFactory = AddSeriesListEntityViewModelFactory( dataSource )//, arguments.listEntityId )

        val seriesListEntityViewModel = ViewModelProviders.of(
            this, viewModelFactory).get(AddSeriesListEntityViewModel::class.java)

        binding.seriesListEntityViewModel = seriesListEntityViewModel
        binding.lifecycleOwner = this


        // Observer for Navigation back to Main List
        seriesListEntityViewModel.navigateToMainList.observe(this, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    AddSeriesListEntityFragmentDirections.actionAddSeriesListEntityFragmentToMainFragment()
                )
                seriesListEntityViewModel.doneNavigatingToMainList()

                // Hide keyboard in case input field is active
                hideKeyboard()
            }
        })

        return binding.root
    }
}