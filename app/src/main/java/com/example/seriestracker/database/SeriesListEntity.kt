package com.example.seriestracker.database

import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.example.seriestracker.LISTING_TYPE_SEASON_PLUS_EPISODE

@Entity(tableName = "series_entity_table")
data class SeriesListEntity (
    @PrimaryKey(autoGenerate = true)
    var entityId: Long = 0L,

    @ColumnInfo(name = "title")
    var title: String = "",

    @ColumnInfo(name = "extras")
    var extras: String = "",

    @ColumnInfo(name = "listingType")
    var listingType: Int = LISTING_TYPE_SEASON_PLUS_EPISODE,

    @ColumnInfo(name = "season")
    var season: Int = 0,

    @ColumnInfo(name = "episode")
    var episode: Int = 0,

    @ColumnInfo(name = "finished")
    var finished: Boolean = false
    )