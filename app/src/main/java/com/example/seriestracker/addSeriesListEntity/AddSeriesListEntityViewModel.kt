package com.example.seriestracker.addSeriesListEntity

import kotlinx.coroutines.*
import android.util.Log
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.example.seriestracker.database.SeriesListEntity
import com.example.seriestracker.database.SeriesListEntityDatabaseDao


class AddSeriesListEntityViewModel(
    val database: SeriesListEntityDatabaseDao
) : ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var currentListEntityID = -1L

    private val title = MutableLiveData<String>()
    private val extras = MutableLiveData<String>()
    private val season = MutableLiveData<Int>()
    private val episode = MutableLiveData<Int>()

    init {
        // initialize values to be empty
        title.value = ""
        extras.value = ""
        season.value = 0
        episode.value = 0
    }

    fun setTitle(t: String) {title.value = t}
    fun setExtras(x: String) {extras.value = x}
    fun setSeason(s: Int) { season.value = s}
    fun setEpisode(e: Int) { episode.value = e}

    private var _navigateToMainList = MutableLiveData<Boolean>()
    val navigateToMainList: LiveData<Boolean>
        get() = _navigateToMainList
    fun doneNavigatingToMainList() { _navigateToMainList.value = false }

    // Click Events
    fun onClickedButtonCancel(){ _navigateToMainList.value = true }

    fun onClickedButtonAdd() {
        uiScope.launch {
            withContext(Dispatchers.IO){
                var newEntity = SeriesListEntity()
                newEntity.title = title.value.toString()
                newEntity.extras = extras.value.toString()
                newEntity.season = season.value!!
                newEntity.episode = episode.value!!
                currentListEntityID = insert(newEntity)
                Log.i("AddSeriesListEntityVM","Added entity. From DB it's values are: ${database.get(currentListEntityID)}")
            }
        }

        _navigateToMainList.value = true
    }


    // Database functions
    private suspend fun update(entity: SeriesListEntity) = withContext(Dispatchers.IO) { database.update(entity) }
    private suspend fun insert(entity: SeriesListEntity) : Long = withContext(Dispatchers.IO) { database.insert(entity) }

}
