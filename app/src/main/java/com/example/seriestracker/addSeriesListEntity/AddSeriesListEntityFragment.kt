package com.example.seriestracker.addSeriesListEntity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import kotlinx.android.synthetic.main.fragment_add_new_series_list_entity.*

class AddSeriesListEntityFragment : Fragment() {

    lateinit var viewModel : AddSeriesListEntityViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View?
    {
        val binding: FragmentAddNewSeriesListEntityBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_add_new_series_list_entity, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = SeriesListEntityDatabase.getInstance(application).seriesListEntityDatabaseDao
        val viewModelFactory = AddSeriesListEntityViewModelFactory( dataSource )//, arguments.listEntityId )
        val seriesListEntityViewModel = ViewModelProviders.of(
            this, viewModelFactory).get(AddSeriesListEntityViewModel::class.java)

        binding.seriesListEntityViewModel = seriesListEntityViewModel
        binding.lifecycleOwner = this
        viewModel = seriesListEntityViewModel

        observeNavigationBackToMainList()

        return binding.root
    }

    // EditText's not available in OnCreateView, but are accessible in OnViewCreated.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeTitle()
        observeExtras()
        observeSeason()
        observeEpisode()
    }

    private fun observeNavigationBackToMainList() {
        viewModel.navigateToMainList.observe(this, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    AddSeriesListEntityFragmentDirections.actionAddSeriesListEntityFragmentToMainFragment()
                )
                viewModel.doneNavigatingToMainList()

                // Hide keyboard in case input field is active
                hideKeyboard()
            }
        })
    }

    private fun observeTitle() {
        addSeriesEnterTitle.addTextChangedListener(object : TextWatcher
        {
            override fun afterTextChanged(p0: Editable?) { viewModel.setTitle(p0.toString()) }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    private fun observeExtras() {
        editSeriesEnterExtras.addTextChangedListener(object : TextWatcher
        {
            override fun afterTextChanged(p0: Editable?) { viewModel.setExtras(p0.toString()) }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    private fun observeSeason() {
        editSeriesEnterSeason.addTextChangedListener(object : TextWatcher
        {
            override fun afterTextChanged(p0: Editable?)
            {
                var seasonText = p0.toString()
                var seasonNum = 0
                if (seasonText.isNotEmpty()) { seasonNum = seasonText.toInt() }
                viewModel.setSeason(seasonNum)
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    private fun observeEpisode() {
        editSeriesEnterEpisode.addTextChangedListener(object : TextWatcher
        {
            override fun afterTextChanged(p0: Editable?)
            {
                var episodeText = p0.toString()
                var episodeNum = 0
                if (episodeText.isNotEmpty()) { episodeNum = episodeText.toInt() }
                viewModel.setEpisode(episodeNum)
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }
}
