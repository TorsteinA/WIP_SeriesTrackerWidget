package com.example.seriestracker.editSeriesListEntity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.seriestracker.database.SeriesListEntityDatabaseDao
import java.lang.IllegalArgumentException

class EditSeriesListEntityViewModelFactory(
    private val listEntityId: Long,
    private val dataSource: SeriesListEntityDatabaseDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditSeriesListEntityViewModel::class.java)) {
            return EditSeriesListEntityViewModel( listEntityId, dataSource ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}