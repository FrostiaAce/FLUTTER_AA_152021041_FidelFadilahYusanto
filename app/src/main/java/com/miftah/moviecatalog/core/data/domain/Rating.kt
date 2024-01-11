package com.miftah.moviecatalog.core.data.domain

import android.os.Parcelable
import com.miftah.moviecatalog.core.data.source.remote.dto.response.RatingsItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class Rating(
    val source: String,
    val value: String
) : Parcelable

fun RatingsItem.convertToRating() : Rating =
    Rating(
        source = source,
        value = value
    )