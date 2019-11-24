package com.example.seriestracker.mainList

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.seriestracker.database.SeriesListEntity
import com.example.seriestracker.database.SeriesListEntityDatabaseDao
import kotlinx.coroutines.*

class MainListViewModel(
    val database: SeriesListEntityDatabaseDao,
    application: Application
) : ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var currentEntity = MutableLiveData<SeriesListEntity?>()
    val entities = database.getAllListEntities()

    // Snackbar
    private var _showSnackbarEvent = MutableLiveData<Long>()
    val showSnackbarEvent: LiveData<Long>
            get() = _showSnackbarEvent
    fun doneShowingSnackbar() { _showSnackbarEvent.value = -1L }

    // Navigation
    private var _navigateToAddNewEntity = MutableLiveData<Boolean>()
    val navigateToAddNewEntity: LiveData<Boolean>
        get() = _navigateToAddNewEntity
    fun doneNavigatingToAddNewEntity() { _navigateToAddNewEntity.value = false }


    // Database functions
    private suspend fun update(entity: SeriesListEntity) {
        withContext(Dispatchers.IO) {
            database.update(entity)
        }
    }

    private suspend fun insert(entity: SeriesListEntity) {
        withContext(Dispatchers.IO) {
            database.insert(entity)
        }
    }


//    init {
//        uiScope.launch {
//            withContext(Dispatchers.IO){
//                //val newEntity = SeriesListEntity()
//                //insert(newEntity)
//            }
//        }
//    }


    /**
     * Execute when the Add Card button is pressed (Later when the Header is clicked)
     * */
    fun onAddNewEntity() {
        _navigateToAddNewEntity.value = true
    }

    fun onClickedEntity(entityId: Long) {
        _showSnackbarEvent.value = entityId
        // TODO Replace with navigation to edit-fragment with entityId as value rather than true
    }

}