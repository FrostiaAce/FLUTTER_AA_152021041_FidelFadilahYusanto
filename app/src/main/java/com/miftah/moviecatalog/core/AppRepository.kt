package com.miftah.moviecatalog.core

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.miftah.moviecatalog.core.data.domain.Bookmark
import com.miftah.moviecatalog.core.data.domain.Comment
import com.miftah.moviecatalog.core.data.domain.Movie
import com.miftah.moviecatalog.core.data.domain.Outcome
import com.miftah.moviecatalog.core.data.domain.convertToMovie
import com.miftah.moviecatalog.core.data.source.local.entity.MovieEntity
import com.miftah.moviecatalog.core.data.source.local.room.MovieDatabase
import com.miftah.moviecatalog.core.data.source.remote.dto.response.ResultResponse
import com.miftah.moviecatalog.core.data.source.remote.retrofit.ApiMovieService
import com.miftah.moviecatalog.utils.Constant.DB_BOOKMARK
import com.miftah.moviecatalog.utils.Constant.DB_COMMENT
import retrofit2.HttpException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

class AppRepository(
    private val apiService: ApiMovieService,
    private val database: MovieDatabase,
    private val context: Context
) {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDatabaseRef = FirebaseDatabase.getInstance()

    fun getAllMovies(query: String): LiveData<PagingData<MovieEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = MoviesRemoteMediator(apiService, database, context, query),
            pagingSourceFactory = {
                database.movieDao().getAllStories()
            }
        ).liveData
    }

    fun getDetailMovie(mbdId: String): LiveData<Outcome<Movie>> = liveData {
        emit(Outcome.Loading)
        try {
            val response = apiService.getMovieDetails(imdbId = mbdId)
            val result = response.convertToMovie()
            emit(Outcome.Success(result))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ResultResponse::class.java)
            val errorMessage = errorBody.message
            Log.d("TAG", "userLogin: $errorMessage")
            emit(Outcome.Error(errorMessage))
        } catch (e: TimeoutException) {
            emit(Outcome.Error("Connection Timeout"))
        } catch (e: UnknownHostException) {
            emit(Outcome.Error("Connection Timeout"))
        }
    }

    fun firebaseAuth() = firebaseAuth

    fun firebaseDbComment() = firebaseDatabaseRef.getReference(DB_COMMENT)

    fun firebaseDbBookmark() = firebaseDatabaseRef.getReference(DB_BOOKMARK)

    fun sendComment(comment: Comment): Boolean {
        var result = true
        firebaseDbComment().push().setValue(comment) { error, _ ->
            result = error == null
        }
        return result
    }

    fun saveBookmark(bookmark: Bookmark): Boolean {
        var result = true
        firebaseDbBookmark().push().setValue(bookmark) { error, ref ->
            result = error == null
            Log.d("adding", ref.key!!)
        }
        return result
    }

    fun unBookmark(mbdId: String, userId: String): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        firebaseDbBookmark().orderByChild("mbdId")
            .equalTo(mbdId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (bookmarkSnapshot in snapshot.children) {
                        val uid = bookmarkSnapshot.child("uid").getValue(String::class.java)

                        if (uid == userId) {
                            bookmarkSnapshot.ref.removeValue()
                            result.postValue(true)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    result.postValue(false)
                }
            })
        return result
    }

    fun isBookmarked(mbdId: String, uidParam: String): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        result.postValue(false)
        firebaseDbBookmark().orderByChild("mbdId")
            .equalTo(mbdId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var exists = false

                    for (bookmarkSnapshot in snapshot.children) {
                        val uid = bookmarkSnapshot.child("uid").getValue(String::class.java)

                        // Check if the uid matches the desired uid
                        if (uid == uidParam) {
                            exists = true
                            break
                        }
                    }

                    result.postValue(exists)
                }

                override fun onCancelled(error: DatabaseError) {
                    throw Exception(error.message)
                }
            })
        return result
    }
}