package com.example.seriestracker.editSeriesListEntity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import com.example.seriestracker.databinding.FragmentEditSeriesListEntityBinding
import com.example.seriestracker.hideKeyboard
import kotlinx.android.synthetic.main.fragment_edit_series_list_entity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EditSeriesListEntityFragment : Fragment()
{
    lateinit var viewModel : EditSeriesListEntityViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View?
    {
        val binding: FragmentEditSeriesListEntityBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_series_list_entity, container, false)

        val application = requireNotNull(this.activity).application
        val arguments = EditSeriesListEntityFragmentArgs.fromBundle(arguments!!)

        val dataSource = SeriesListEntityDatabase.getInstance(application).seriesListEntityDatabaseDao
        val viewModelFactory = EditSeriesListEntityViewModelFactory( arguments.entityId, dataSource)
        val seriesListEntityViewModel = ViewModelProviders.of(
            this, viewModelFactory).get(EditSeriesListEntityViewModel::class.java)

        binding.seriesListEntityViewModel = seriesListEntityViewModel
        binding.lifecycleOwner = this
        viewModel = seriesListEntityViewModel

        observeNavigationBackToMainList()

        return binding.root
    }

    // EditText's not available in OnCreateView, but are accessible in OnViewCreated.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeEditValues()
        observeInitializeValues()

        // Set some unused elements to invisible    // TODO Implement needed things to remove this
        setInivisibleElements()
    }

    private fun setInivisibleElements(){
            //editSeriesListingTypeText.visibility = View.INVISIBLE
            editSeriesListingTypeHolder.visibility = View.INVISIBLE

            //editSeriesFinishedText.visibility = View.INVISIBLE
            seriesEntityFinishedChipGroup.visibility = View.INVISIBLE
    }

    // Observer for navigation
    private fun observeNavigationBackToMainList() {
        viewModel.navigateToMainList.observe(this, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    EditSeriesListEntityFragmentDirections.actionEditSeriesListEntityFragmentToMainListFragment()
                )
                viewModel.doneNavigatingToMainList()

                // Hide keyboard in case input field is active
                hideKeyboard()
            }
        })
    }

    // Observers for initializing EditText values
    private fun observeInitializeValues() {
        observeInitializeValueTitle()
        observeInitializeValueExtras()
        observeInitializeValueSeason()
        observeInitializeValueEpisode()
    }

    private fun observeInitializeValueTitle(){
        viewModel.initialEntityTitle.observe(this, Observer { title ->
            if (title != null){
                Log.i("EditFrag","Setting Title to $title")
                editSeriesEnterTitle.setText(title)
                viewModel.doneInitiatingTitle()
            }
        })
    }

    private fun observeInitializeValueExtras(){
        viewModel.initialEntityExtras.observe(this, Observer { extras ->
            if (extras != null){
                Log.i("EditFrag","Setting Extras to $extras")
                editSeriesEnterExtras.setText(extras)
                viewModel.doneInitiatingExtras()
            }
        })
    }

    private fun observeInitializeValueSeason(){
        viewModel.initialEntitySeason.observe(this, Observer { season ->
            if (season != null){
                Log.i("EditFrag","Setting Season to $season")
                editSeriesEnterSeason.setText(season)
                viewModel.doneInitiatingSeason()
            }
        })
    }

    private fun observeInitializeValueEpisode(){
        viewModel.initialEntityEpisode.observe(this, Observer { episode ->
            if (episode != null){
                Log.i("EditFrag","Setting Episode to $episode")
                editSeriesEnterEpisode.setText(episode)
                viewModel.doneInitiatingEpisode()
            }
        })
    }

    // Observers for EditTexts
    private fun observeEditValues(){
        observeTitleText()
        observeExtrasText()
        observeSeasonText()
        observeEpisodeText()
    }

    private fun observeTitleText() {
        editSeriesEnterTitle.addTextChangedListener(object : TextWatcher
        {
            override fun afterTextChanged(p0: Editable?) { viewModel.setTitle(p0.toString()) }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    private fun observeExtrasText() {
        editSeriesEnterExtras.addTextChangedListener(object : TextWatcher
        {
            override fun afterTextChanged(p0: Editable?) { viewModel.setExtras(p0.toString()) }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    private fun observeSeasonText() {
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

    private fun observeEpisodeText() {
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
