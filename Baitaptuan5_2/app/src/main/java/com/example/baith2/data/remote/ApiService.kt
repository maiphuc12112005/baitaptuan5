package com.example.baith2.data.remote

import com.example.baith2.data.model.Product
import retrofit2.http.GET

interface ApiService {
    @GET("m1/890655-872447-default/v2/product")
    suspend fun getProduct(): Product
}