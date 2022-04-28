package com.max.guess

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShareViewModel :ViewModel(){

    var count : MutableLiveData<Int> = MutableLiveData<Int>(0)

    fun add(n:Int){
        count.value = n
    }
}