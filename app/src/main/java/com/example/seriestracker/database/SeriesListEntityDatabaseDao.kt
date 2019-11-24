package com.example.seriestracker.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SeriesListEntityDatabaseDao {

    @Insert
    fun insert(entity: SeriesListEntity) : Long

    @Update
    fun update(entity: SeriesListEntity)

    @Delete
    fun delete(entity: SeriesListEntity)

    @Query("SELECT * from series_entity_table WHERE entityId = :key")
    fun get(key: Long): SeriesListEntity?

    /**
     * Selects and returns all rows in the table,
     *
     * sorted by start time in descending order.
     */
    @Query("SELECT * FROM series_entity_table ORDER BY entityId DESC")
    fun getAllListEntities(): LiveData<List<SeriesListEntity>>


    /**
     * Selects and returns the night with given nightId.
     */
    @Query("SELECT * from series_entity_table WHERE entityId = :key")
    fun getListEntityWithId(key: Long): LiveData<SeriesListEntity>

}