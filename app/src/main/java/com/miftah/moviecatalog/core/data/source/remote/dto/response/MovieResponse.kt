package com.miftah.moviecatalog.core.data.source.remote.dto.response

import com.google.gson.annotations.SerializedName

data class MovieResponse(

	@field:SerializedName("Metascore")
	val metascore: String,

	@field:SerializedName("BoxOffice")
	val boxOffice: String,

	@field:SerializedName("Website")
	val website: String,

	@field:SerializedName("imdbRating")
	val imdbRating: String,

	@field:SerializedName("imdbVotes")
	val imdbVotes: String,

	@field:SerializedName("Ratings")
	val ratings: List<RatingsItem>,

	@field:SerializedName("Runtime")
	val runtime: String,

	@field:SerializedName("Language")
	val language: String,

	@field:SerializedName("Rated")
	val rated: String,

	@field:SerializedName("Production")
	val production: String,

	@field:SerializedName("Released")
	val released: String,

	@field:SerializedName("imdbID")
	val imdbID: String,

	@field:SerializedName("Plot")
	val plot: String,

	@field:SerializedName("Director")
	val director: String,

	@field:SerializedName("Title")
	val title: String,

	@field:SerializedName("Actors")
	val actors: String,

	@field:SerializedName("Response")
	val response: String,

	@field:SerializedName("Type")
	val type: String,

	@field:SerializedName("Awards")
	val awards: String,

	@field:SerializedName("DVD")
	val dVD: String,

	@field:SerializedName("Year")
	val year: String,

	@field:SerializedName("Poster")
	val poster: String,

	@field:SerializedName("Country")
	val country: String,

	@field:SerializedName("Genre")
	val genre: String,

	@field:SerializedName("Writer")
	val writer: String
)

data class RatingsItem(

	@field:SerializedName("Value")
	val value: String,

	@field:SerializedName("Source")
	val source: String
)
