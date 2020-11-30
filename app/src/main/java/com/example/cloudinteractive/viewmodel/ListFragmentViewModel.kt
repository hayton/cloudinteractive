package com.example.cloudinteractive.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cloudinteractive.model.Photo
import com.example.cloudinteractive.repository.Repository

class ListFragmentViewModel @ViewModelInject constructor(private val repository: Repository): ViewModel() {

    var photosMutableLiveData = repository.getPhotos()

    fun reload() {
        photosMutableLiveData = repository.getPhotos()
    }

}