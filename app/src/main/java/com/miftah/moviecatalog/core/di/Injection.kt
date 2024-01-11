package com.miftah.moviecatalog.core.di

import android.content.Context
import com.miftah.moviecatalog.core.AppRepository
import com.miftah.moviecatalog.core.data.source.local.room.MovieDatabase
import com.miftah.moviecatalog.core.data.source.remote.retrofit.ApiMovieConfig

object Injection {
    fun provideRepository(context: Context): AppRepository {
        val database = MovieDatabase.getDatabase(context)
        val apiService = ApiMovieConfig.getApiService()
        return AppRepository(apiService, database, context)
    }
}