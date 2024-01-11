package com.miftah.moviecatalog.core.data.source.local.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.miftah.moviecatalog.core.data.source.local.entity.MovieEntity

@Dao
interface MoviesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movie: List<MovieEntity>)

    @Query("SELECT * FROM movies")
    fun getAllStories(): PagingSource<Int, MovieEntity>

    @Query("DELETE FROM movies")
    suspend fun deleteAll()
}