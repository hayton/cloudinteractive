package com.example.cloudinteractive.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cloudinteractive.model.Photo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class Repository @Inject constructor() {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val service by lazy {
        retrofit.create(ApiService::class.java)
    }

    fun getPhotos(): MutableLiveData<ArrayList<Photo>> {
        val photoLiveData = MutableLiveData<ArrayList<Photo>>()
        service.getPhotos().enqueue(object : Callback<ArrayList<Photo>> {
            override fun onResponse(
                call: Call<ArrayList<Photo>>,
                response: Response<ArrayList<Photo>>
            ) {
                Log.d("Repository", "onresponse")
                if (response.isSuccessful) {
                    photoLiveData.value = response.body()
                } else {
                    photoLiveData.value = null
                }
            }

            override fun onFailure(call: Call<ArrayList<Photo>>, t: Throwable) {
                Log.d("Repository", "$t")
                photoLiveData.value = null
            }

        })
        return photoLiveData
    }

}