package com.example.wastemangement.Fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileFragmentViewModel : ViewModel(){
    val name = MutableLiveData<String>()
    fun setName(newData : String){
        name.value = newData
    }
    val image = MutableLiveData<String>()
    fun setImage(newData: String){
        image.value = newData
    }
    val address = MutableLiveData<String>()
    fun setAddress(newData: String){
        address.value = newData
    }
}