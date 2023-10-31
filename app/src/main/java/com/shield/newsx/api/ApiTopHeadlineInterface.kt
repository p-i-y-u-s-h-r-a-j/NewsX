package com.shield.newsx.api

import com.shield.newsx.models.ResponseNews
import retrofit2.Response
import retrofit2.http.GET

interface ApiTopHeadlineInterface {

    @GET("/v2/top-headlines?country=In&apiKey=d6085022f73741f488d6f4d665bb37c1")
    suspend fun getTopHeadline(): Response<ResponseNews>
}