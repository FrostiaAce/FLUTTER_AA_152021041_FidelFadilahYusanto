package com.miftah.moviecatalog.core.data.source.remote.dto.response

import com.google.gson.annotations.SerializedName

data class ResultResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)
