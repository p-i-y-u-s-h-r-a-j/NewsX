package com.shield.newsx.api

import com.shield.newsx.models.ResponseNews
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiCategoryInterface {
    @GET("/v2/everything")
    suspend fun getCategoryData(@Query("q") userInput: String, @Query("apiKey") apiKey: String): Response<ResponseNews>
}