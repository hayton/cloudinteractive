package com.example.cloudinteractive.repository

import com.example.cloudinteractive.model.Photo
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("photos")
    fun getPhotos(): Call<ArrayList<Photo>>

}