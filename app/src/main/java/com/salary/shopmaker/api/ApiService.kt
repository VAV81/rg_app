package com.salary.shopmaker.api

import com.salary.shopmaker.model.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("products/search")
    suspend fun getProducts(
        @Query("fields") fields: String = "FULL",
        @Query("currentPage") currentPage: Int,
        @Query("pageSize") pageSize: Int,
        @Query("categoryCode") categoryCode: String = "Perfumery"
    ): ApiResponse
}