package com.max.guess

import android.util.Log
import android.view.View
import android.widget.Toast

class ClickHandle {

    fun onClick(v: View){
        // 假設有多個按鈕時,可以使用when(v?.id){    }
        when(v?.id){
            R.id.bt_click ->{
                Log.e("TAG", "DataBinding 測試按鈕點擊 !")
            }
        }
    }

    fun onClick(v: View , n:Int){
        // 假設有多個按鈕時,可以使用when(v?.id){    }
        when(v?.id){
            R.id.bt_click1 ->{
                Log.e("TAG", "DataBinding 測試按鈕並且傳整數值! $n")
            }
        }
    }

}