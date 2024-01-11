package com.miftah.moviecatalog.core

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.miftah.moviecatalog.core.data.domain.Movies
import com.miftah.moviecatalog.core.data.domain.convertToMovies
import com.miftah.moviecatalog.core.data.domain.convertToMoviesEntity
import com.miftah.moviecatalog.core.data.source.local.entity.MovieEntity
import com.miftah.moviecatalog.core.data.source.local.entity.RemoteKeysEntity
import com.miftah.moviecatalog.core.data.source.local.room.MovieDatabase
import com.miftah.moviecatalog.core.data.source.remote.retrofit.ApiMovieService
import com.miftah.moviecatalog.utils.hasInternetConnection
import retrofit2.HttpException

@OptIn(ExperimentalPagingApi::class)
class MoviesRemoteMediator(
    private val apiService: ApiMovieService,
    private val database: MovieDatabase,
    private val context: Context,
    private val searchQuery: String
) : RemoteMediator<Int, MovieEntity>() {

    private val remoteKeyDao = database.remoteKeysDao()
    private val moviesDao = database.movieDao()
    private lateinit var responseData: List<Movies>

    override suspend fun initialize(): InitializeAction {
        return if (hasInternetConnection(context)) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            InitializeAction.SKIP_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            responseData = if (searchQuery.isNotEmpty()) {
                apiService.getMovies(
                    s = searchQuery,
                    page = page
                ).search.map { it.convertToMovies() }
            } else {
                mutableListOf()
            }

            val endOfPaginationReached = responseData.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeyDao.deleteRemoteKeys()
                    moviesDao.deleteAll()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = responseData.map {
                    RemoteKeysEntity(prevKey = prevKey, nextKey = nextKey)
                }
                remoteKeyDao.insertAll(keys)
                moviesDao.insertMovies(responseData.map { it.convertToMoviesEntity() })
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }


    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, MovieEntity>): RemoteKeysEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            remoteKeyDao.getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, MovieEntity>): RemoteKeysEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            remoteKeyDao.getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, MovieEntity>): RemoteKeysEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                remoteKeyDao.getRemoteKeysId(id)
            }
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}