package com.example.cloudinteractive.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cloudinteractive.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL

class ListFragmentViewModel @ViewModelInject constructor(private val repository: Repository): ViewModel() {

    var photosMutableLiveData = repository.getPhotos()
    var thumbnailMap = mutableMapOf<Int, Bitmap>()
    var updateViewHolderLiveData = MutableLiveData<Int>()

    fun getThumbnail(position: Int, thumbnailUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val connection = URL(thumbnailUrl).openConnection().apply {
                setRequestProperty("User-Agent", "custom-agent")
            }
            val bitmap = BitmapFactory.decodeStream(connection.getInputStream())
            thumbnailMap[position] = bitmap
            launch(Dispatchers.Main) {
                updateViewHolderLiveData.value = position
            }

        }


    }

    fun reload() {
        photosMutableLiveData = repository.getPhotos()
    }

}