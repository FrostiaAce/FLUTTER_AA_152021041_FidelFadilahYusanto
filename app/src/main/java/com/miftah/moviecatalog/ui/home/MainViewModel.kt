package com.miftah.moviecatalog.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.miftah.moviecatalog.core.AppRepository
import com.miftah.moviecatalog.core.data.domain.Bookmark
import com.miftah.moviecatalog.core.data.domain.Comment
import com.miftah.moviecatalog.core.data.source.local.entity.MovieEntity

class MainViewModel(private val repo: AppRepository) : ViewModel() {

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> get() = _searchQuery

    val pagingData: LiveData<PagingData<MovieEntity>> = _searchQuery.switchMap { query ->
        repo.getAllMovies(query).cachedIn(viewModelScope)
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun getMovieDetailById(mbdId: String) = repo.getDetailMovie(mbdId)

    fun firebaseAuth() = repo.firebaseAuth()

    fun sendComment(comment: Comment) = repo.sendComment(comment)

    fun firebaseDbComment() = repo.firebaseDbComment()

    fun firebaseSaveBookmark(bookmark: Bookmark) = repo.saveBookmark(bookmark)

    fun firebaseUnBookmark(mbdId: String, userId : String) = repo.unBookmark(mbdId, userId)

    fun firebaseQuery(mbdId: String, userId : String) = repo.isBookmarked(mbdId, userId)

    fun firebaseDbBookmark() = repo.firebaseDbBookmark()
}