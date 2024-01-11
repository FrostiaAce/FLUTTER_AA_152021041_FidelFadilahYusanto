package com.miftah.moviecatalog.core.data.domain

import android.os.Parcelable
import com.miftah.moviecatalog.core.data.source.local.entity.MovieEntity
import com.miftah.moviecatalog.core.data.source.remote.dto.response.SearchItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movies(
    val poster: String,
    val title: String,
    val type: String,
    val year: String,
    val imdbID: String
) : Parcelable

fun SearchItem.convertToMovies(): Movies =
    Movies(
        poster = poster,
        title = title,
        type = type,
        year = year,
        imdbID = imdbID
    )

fun Movies.convertToMoviesEntity() : MovieEntity =
    MovieEntity(
        id = imdbID,
        poster = poster,
        title = title,
        type = type,
        year = year,
    )