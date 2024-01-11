package com.miftah.moviecatalog.utils

import android.content.Context
import android.net.ConnectivityManager

object Constant {

    const val BASE_MOVIE_URL = "http://www.omdbapi.com"

    const val BASE_MOVIE_IMG_URL = "http://img.omdbapi.com"

    const val MOVIE_KEY = "b1b57d9e"

    const val DB_COMMENT = "DB_COMMENT"
    const val DB_BOOKMARK = "DB_BOOKMARK"
}

fun hasInternetConnection(context : Context): Boolean {
    val connectivityManager =
        context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}