package com.miftah.moviecatalog.core.data.domain

sealed class Outcome<out R> private constructor() {
    data class Success<out T>(val data: T) : Outcome<T>()
    data class Error(val error: String) : Outcome<Nothing>()
    data object Loading : Outcome<Nothing>()
}