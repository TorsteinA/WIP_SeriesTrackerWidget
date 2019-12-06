package com.example.seriestracker.editSeriesListEntity
import android.os.Debug
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
    private var currentListEntityID = entityId
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
//    private var _title = MutableLiveData<String>()
//    private val _extras = MutableLiveData<String>()
//    private val _season = MutableLiveData<Int>()
//    private val _episode = MutableLiveData<Int>()

    private var _currentEntity = MutableLiveData<SeriesListEntity?>()
    val currentEntity: LiveData<SeriesListEntity?>
        get() = _currentEntity
    fun doneEntiting() { _currentEntity.value = null }

    private var _initialEntityTitle = MutableLiveData<String>()
    val initialEntityTitle: LiveData<String>
        get() = _initialEntityTitle
    fun doneInitiatingTitle() { _initialEntityTitle.value = null }



    init {
        setInitialValues()
    }

    private fun setInitialValues(){
        uiScope.launch {
            withContext(Dispatchers.IO){
                val entity = get(entityId)
                if (entity != null){
                    //_currentEntity.postValue(entity)
                    _initialEntityTitle.postValue(entity.title)
                    Log.i("EditVM", "Entity not null $entity with title ${entity.title}" )
//                    _title.postValue(entity.title)
//                    Log.i("EditVM", "Title is ${_title.value} and ${entity.title}")
//                    _extras.postValue(entity.extras)
//                    _season.postValue(entity.season)
//                    _episode.postValue(entity.episode)
                }
            }
        }

    }


//    fun setTitle(t: String) {_title.value = t}
//    fun setExtras(x: String) {_extras.value = x}
//    fun setSeason(s: Int) { _season.value = s}
//    fun setEpisode(e: Int) { _episode.value = e}

    fun setTitle(t: String) {_currentEntity.value?.title = t}
    fun setExtras(x: String) {_currentEntity.value?.extras = x}
    fun setSeason(s: Int) { _currentEntity.value?.season = s}
    fun setEpisode(e: Int) { _currentEntity.value?.episode = e}

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
//                    if (_title.value != null) newEntity.title = _title.value.toString()
//                    if (_extras.value != null) newEntity.extras = _extras.value.toString()
//                    if (_season.value != null) newEntity.season = _season.value!!
//                    if (_episode.value != null) newEntity.episode = _episode.value!!
                    newEntity.title = _currentEntity.value!!.title
                    update(newEntity)
                }
            }
        }

        _navigateToMainList.value = true
    }



    // Database functions
    private suspend fun get(entityId: Long) = withContext(Dispatchers.IO) { database.get(entityId)}
    private suspend fun getListEntityWithId(entityId: Long) = withContext(Dispatchers.IO) { database.getListEntityWithId(entityId)}
    private suspend fun update(entity: SeriesListEntity) = withContext(Dispatchers.IO) { database.update(entity) }
    private suspend fun insert(entity: SeriesListEntity) : Long = withContext(Dispatchers.IO) { database.insert(entity) }

}
