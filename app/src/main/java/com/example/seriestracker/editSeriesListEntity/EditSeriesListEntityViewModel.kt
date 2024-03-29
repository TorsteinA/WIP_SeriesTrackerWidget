package com.example.seriestracker.editSeriesListEntity
import android.util.Log
import kotlinx.coroutines.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.example.seriestracker.database.SeriesListEntity
import com.example.seriestracker.database.SeriesListEntityDatabaseDao


class EditSeriesListEntityViewModel(
    private val entityId: Long = -1L,
    datasource: SeriesListEntityDatabaseDao
) : ViewModel() {

    val database = datasource
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    // Entity data values
    private var _title = MutableLiveData<String>()
    private val _extras = MutableLiveData<String>()
    private val _season = MutableLiveData<Int>()
    private val _episode = MutableLiveData<Int>()
    private val _finished = MutableLiveData<Boolean>()

    // Setters
    fun setTitle(t: String) {_title.value = t}
    fun setExtras(x: String) {_extras.value = x}
    fun setSeason(s: Int) { _season.value = s}
    fun setEpisode(e: Int) { _episode.value = e}
    fun setFinished(fin: Boolean) { _finished.value = fin }

    // Initialize Title in EditText
    private var _initialEntityTitle = MutableLiveData<String>()
    val initialEntityTitle: LiveData<String>
        get() = _initialEntityTitle
    fun doneInitiatingTitle() { _initialEntityTitle.value = null }

    // Initialize Extras in EditText
    private var _initialEntityExtras = MutableLiveData<String>()
    val initialEntityExtras: LiveData<String>
        get() = _initialEntityExtras
    fun doneInitiatingExtras() { _initialEntityExtras.value = null }

    // Initialize Season in EditText
    private var _initialEntitySeason = MutableLiveData<String>()
    val initialEntitySeason: LiveData<String>
        get() = _initialEntitySeason
    fun doneInitiatingSeason() { _initialEntitySeason.value = null }

    // Initialize Episode in EditText
    private var _initialiEntityEpisode = MutableLiveData<String>()
    val initialEntityEpisode: LiveData<String>
        get() = _initialiEntityEpisode
    fun doneInitiatingEpisode() { _initialiEntityEpisode.value = null }

    // Initialize Finished in ChipGroup
    private var _initialEntityFinished = MutableLiveData<Boolean>()
    val initialEntityFinished: LiveData<Boolean>
        get() = _initialEntityFinished
    fun doneInitiatingFinished() { _initialEntityFinished.value = null }


    init { setInitialValues() }

    private fun setInitialValues(){
        uiScope.launch {
            withContext(Dispatchers.IO){
                val entity = get(entityId)
                if (entity != null){
                    _initialEntityTitle.postValue(entity.title)
                    _initialEntityExtras.postValue(entity.extras)
                    _initialEntitySeason.postValue(entity.season.toString())
                    _initialiEntityEpisode.postValue(entity.episode.toString())
                    _initialEntityFinished.postValue(entity.finished)
                }
            }
        }
    }

    // Navigation back to main list
    private var _navigateToMainList = MutableLiveData<Boolean>()
    val navigateToMainList: LiveData<Boolean>
        get() = _navigateToMainList
    fun doneNavigatingToMainList() { _navigateToMainList.value = false }


    // Click Events
    fun onClickedButtonCancel(){ _navigateToMainList.value = true }

    fun onClickedButtonEdit() {
        uiScope.launch {
            withContext(Dispatchers.IO){
                var newEntity = get(entityId)
                if (newEntity != null) {
                    if (_title.value != null) newEntity.title = _title.value.toString()
                    if (_extras.value != null) newEntity.extras = _extras.value.toString()
                    if (_season.value != null) newEntity.season = _season.value!!
                    if (_episode.value != null) newEntity.episode = _episode.value!!
                    if (_finished.value != null) newEntity.finished = _finished.value!!
                    update(newEntity)
                    Log.i("EditVM","Edited Entity $newEntity")
                }
            }
        }

        _navigateToMainList.value = true
    }

    fun onClickedButtonDelete(){
        uiScope.launch {
            withContext(Dispatchers.IO){
                val entity = get(entityId)
                if (entity != null) { delete(entity) }
            }
        }

        _navigateToMainList.value = true
    }


    // Database functions
    private suspend fun get(entityId: Long) = withContext(Dispatchers.IO) { database.get(entityId)}
    private suspend fun update(entity: SeriesListEntity) = withContext(Dispatchers.IO) { database.update(entity) }
    private suspend fun delete(entity: SeriesListEntity) = withContext(Dispatchers.IO) { database.delete(entity) }
}
