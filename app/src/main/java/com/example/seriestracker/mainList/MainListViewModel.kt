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
    val entities = database.getAllListEntities()

    private var _showSnackbarEvent = MutableLiveData<String>()
    val showSnackbarEvent: LiveData<String>
        get() = _showSnackbarEvent
    fun doneShowingSnackbar() { _showSnackbarEvent.value = "" }

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

    private suspend fun get(entity: SeriesListEntity) {
        withContext(Dispatchers.IO) {
            database.get(entity.entityId)
        }
    }

    /**
     * Execute when the Add Card button is pressed (Later when the Header is clicked)
     * */
    fun onAddNewEntity() { _navigateToAddNewEntity.value = true }

    fun onClickedEntity(entityId: Long) { _showSnackbarEvent.value = "Clicked Entity with id: $entityId" }

    fun onClickedText(entityId: Long) { _showSnackbarEvent.value = "Clicked Text Area of id: $entityId" }

    fun onClickedEpisode(entityId: Long) { _showSnackbarEvent.value = "Clicked Episode of id: $entityId" }

    fun onClickedSeason(entityId: Long) { _showSnackbarEvent.value = "Clicked Season of id: $entityId" }

    fun onClickedCheckmark(entityId: Long) { _showSnackbarEvent.value = "Clicked Checkmark of id: $entityId" }


    /*fun onIncrementEpisode(entityId: Long) {
        uiScope.launch {
            withContext(Dispatchers.IO){
                val entity = database.getListEntityWithId(entityId)
                val episode = entity.value?.episode
                if (episode != null) entity.value?.episode = episode
                if (entity.value != null) database.update(entity.value!!)
            }
        }
    }*/

}