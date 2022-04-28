package com.max.guess

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LiveDataViewModel :ViewModel(){

    val nameLd : MutableLiveData<String> by lazy { MutableLiveData<String>() }

    fun updateName(name : String){
        nameLd.value = name
    }

    fun updataNameThread(name : String){
        nameLd.postValue(name) // postValue()方法 ,可以在子執行緒中更新 , setValue是不行的
    }
}