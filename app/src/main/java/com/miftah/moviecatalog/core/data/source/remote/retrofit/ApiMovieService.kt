package com.miftah.moviecatalog.core.data.source.remote.retrofit

import com.miftah.moviecatalog.core.data.source.remote.dto.response.MovieResponse
import com.miftah.moviecatalog.core.data.source.remote.dto.response.MoviesResponse
import com.miftah.moviecatalog.utils.Constant
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiMovieService {

    @GET("/")
    suspend fun getMovies(
        @Query("s") s: String,
        @Query("page") page: Int,
        @Query("apikey") apikey : String = Constant.MOVIE_KEY,
    ): MoviesResponse

    @GET("/")
    suspend fun getMovieDetails(
        @Query("i") imdbId: String,
        @Query("apikey") apikey : String = Constant.MOVIE_KEY,
    ): MovieResponse
}