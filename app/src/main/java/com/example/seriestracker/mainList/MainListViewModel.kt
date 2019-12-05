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

    // Snackbar
    private var _showSnackbarEvent = MutableLiveData<String>()
    val showSnackbarEvent: LiveData<String>
        get() = _showSnackbarEvent
    fun doneShowingSnackbar() { _showSnackbarEvent.value = "" }

    // Navigation
    private var _navigateToAddNewEntity = MutableLiveData<Boolean>()
    val navigateToAddNewEntity: LiveData<Boolean>
        get() = _navigateToAddNewEntity
    fun doneNavigatingToAddNewEntity() { _navigateToAddNewEntity.value = false }

    private var _navigateToEditEntity = MutableLiveData<Boolean>()
    val navigateToEditEntity: LiveData<Boolean>
        get() = _navigateToEditEntity
    fun doneNavigatingToEditEntity() { _navigateToEditEntity.value = false }

    /**
     * Execute when the Add Card button is pressed (Later when the Header is clicked)
     * */
    fun onAddNewEntity() { _navigateToAddNewEntity.value = true }

    fun onClickedText(entityId: Long) { _showSnackbarEvent.value = "Clicked Text Area of id: $entityId" }  // TODO Add navigateToEditEntity

    fun onClickedEpisode(entityId: Long) { onIncrementEpisode(entityId) }

    fun onClickedSeason(entityId: Long) { onIncrementSeason(entityId) }

    fun onClickedCheckmark(entityId: Long) { onFinish(entityId) } //_showSnackbarEvent.value = "Clicked Checkmark of id: $entityId" }  // TODO Add onClickedCheckmark

    private fun onIncrementEpisode(entityId: Long) {
        uiScope.launch {
            withContext(Dispatchers.IO){
                val entity = database.get(entityId)
                val episode = entity?.episode
                if (episode != null) entity.episode = episode +1
                if (entity != null) database.update(entity)
            }
        }
    }

    private fun onIncrementSeason(entityId: Long) {
        uiScope.launch {
            withContext(Dispatchers.IO){
                val entity = database.get(entityId)
                val season = entity?.season
                val episode = entity?.episode
                if (season != null && episode != null)
                {
                    entity.season = season +1
                    entity.episode = 1
                }

                if (entity != null) database.update(entity)
            }
        }
    }

    private fun onFinish(entityId: Long) {
        uiScope.launch {
            withContext(Dispatchers.IO){
                val entity =  database.get(entityId)
                val finished = entity?.finished
                if (finished != null) entity.finished = !finished
                if (entity != null) database.update(entity)
            }
        }

        // TODO if finished is true, change checkmark to small square icon, and if it's false, change to checkmark icon


    }
}