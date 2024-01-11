package com.miftah.moviecatalog.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey
    @ColumnInfo("imdbID")
    val id: String,

    @ColumnInfo("poster")
    val poster: String,

    @ColumnInfo("title")
    val title: String,

    @ColumnInfo("type")
    val type: String,

    @ColumnInfo("year")
    val year: String,
)
