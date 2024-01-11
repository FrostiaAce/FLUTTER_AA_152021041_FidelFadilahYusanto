package com.miftah.moviecatalog.core.data.domain

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Comment(
    val sender: String? = null,
    val timestamp: Long? = null,
    val text: String? = null,
    val photoUrl: String? = null,
    val mdbId : String? = null
)
