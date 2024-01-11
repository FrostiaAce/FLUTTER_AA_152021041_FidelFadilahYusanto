package com.miftah.moviecatalog.core.data.source.remote.dto.response

import com.google.gson.annotations.SerializedName

data class MoviesResponse(

    @field:SerializedName("Response")
    val response: String,

    @field:SerializedName("totalResults")
    val totalResults: String,

    @field:SerializedName("Search")
    val search: List<SearchItem>
)

data class SearchItem(

    @field:SerializedName("Type")
    val type: String,

    @field:SerializedName("Year")
    val year: String,

    @field:SerializedName("imdbID")
    val imdbID: String,

    @field:SerializedName("Poster")
    val poster: String,

    @field:SerializedName("Title")
    val title: String
)

