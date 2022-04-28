package com.max.guess

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShareViewModel :ViewModel(){
    /**
     * LiveData是一種可觀察的數據存儲器類。與常規的可觀察類不同，LiveData 具有生命週期感知能力，意指它遵循其他應用組件（如Activity、Fragment 或Service）的生命週期。
     * 這種感知能力可確保LiveData 僅更新處於活躍生命週期狀態的應用組件觀察者。
     * MutableLiveData 搭配 observe()方法一起使用
     */
    var count : MutableLiveData<Int> = MutableLiveData<Int>(0) // 要給預設值 0 , 不然啟用時會顯示null

    fun add(n:Int){
        count.value = n
    }
}