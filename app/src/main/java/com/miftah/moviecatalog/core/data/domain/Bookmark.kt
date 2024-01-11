package com.miftah.moviecatalog.core.data.domain

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Bookmark(
    val title: String? = null,
    val poster: String? = null,
    val mbdId: String? = null,
    val year: String? = null,
    val uid: String? = null,
)
