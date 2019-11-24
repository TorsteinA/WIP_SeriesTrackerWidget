package com.example.seriestracker.addSeriesListEntity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.seriestracker.database.SeriesListEntityDatabaseDao
import java.lang.IllegalArgumentException

class AddSeriesListEntityViewModelFactory(
    private val dataSource: SeriesListEntityDatabaseDao//,
    //private val listEntityId: Long
) : ViewModelProvider.Factory {
    @Suppress("unchecked cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddSeriesListEntityViewModel::class.java)){
            return AddSeriesListEntityViewModel(
                dataSource//, listEntityId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}