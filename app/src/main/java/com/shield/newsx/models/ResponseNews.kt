package com.shield.newsx.models

import com.google.gson.annotations.SerializedName

data class ResponseNews(

	@field:SerializedName("totalResults")
	val totalResults: Int? = null,

	@field:SerializedName("articles")
	val articles: List<ArticlesItem>?,

	@field:SerializedName("status")
	val status: String? = null
)