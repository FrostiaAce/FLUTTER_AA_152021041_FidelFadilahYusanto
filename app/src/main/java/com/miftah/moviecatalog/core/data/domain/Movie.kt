package com.miftah.moviecatalog.core.data.domain

import android.os.Parcelable
import com.miftah.moviecatalog.core.data.source.remote.dto.response.MovieResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val genre: String,
    val ratingList: List<Rating>,
    val released: String,
    val response: String,
    val title: String,
    val year: String,
    val imdbID: String,
    val imdbRating: String,
    val imdbVotes: String,
    val actors: String,
    val plot : String
) : Parcelable


fun MovieResponse.convertToMovie() : Movie =
    Movie (
        genre = genre,
        ratingList = ratings.map { it.convertToRating() },
        released = released,
        response = response,
        title = title,
        year = year,
        imdbID = imdbID,
        imdbRating = imdbRating,
        imdbVotes = imdbVotes,
        actors = actors,
        plot = plot
    )


