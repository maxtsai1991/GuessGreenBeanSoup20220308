package com.max.guess

import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

/**
 *  @Bindable : 註解的意思,只會用在實作 BaseObservable Or Observable中 , 才能使用@Bindable
 *  @Bindable   這個註解會在 Project選項裡的 > build > generated > source > kapt > debug > com.max.guess > generated.callback > 生成BR類別
 */
class RememberMe : BaseObservable(){

    private var flag : Boolean = false

    @Bindable
    fun getRememberMe(): Boolean {
        return flag
    }

    fun setRememberMe(value: Boolean){
        Log.e("TAG","isChecked = $value")
        /**
         * 當進來的value布林值,不等於flag時,則改變flag值
         */
        if(flag != value){
            flag = value
            notifyPropertyChanged(BR.rememberMe) // BR.rememberMe 能使用這行需添加@Bindable
        }
    }


}