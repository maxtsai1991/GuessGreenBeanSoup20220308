package com.max.guess

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
    /**
     * 建立GuessViewModel的說明:
     * 1. 建立ViewModel 注意要繼承ViewModel類別(選androidx.lifecycle)
     * 2. 繼承ViewModel類別 必須要呼叫父類別的建構子 ,所以要用括號 ( )
     * 3. 設計需求 EX : counter(猜的次數) , ckeck方法(比較數字的結果)
     * 4. 設計完後, ViewModel 傳遞(傳遞使用MutableLiveData)給前面的Activty ,去通知前面的畫面,我的資料需要改變
     */
class GuessViewModel : ViewModel(){

        /**
         * val counter = MutableLiveData<Int>()的說明:
         * 1.建立counter屬性資料,到畫面上
         * 2.MutableLiveData(androidx.lifecycle) : 能夠改變資料MutableLiveData,使用MutableLiveData是因為能夠產生物件,泛型符號裡面給型態,這個案例是counter所以給Int
         * 3.在Activity裡面有人去觀察counter的值,當counter在這個地方有改變,在Activity那裏就可以得到通知而被執行
         */
    val counter = MutableLiveData<Int>()

        /**
         *  init { } 說明:
         *  1. 初始化執行程式的方法
         *  2. 初始化counter
         *  3. 使用.value 是因為MutableLiveData泛型裡面給Int,此時的LiveData不叫Data叫value
         */
    init {
        counter.value = 0
    }

        /**
         * fun guess(num: Int){}此方法 等於 SecretNumber.kt的validate方法 ,畫面是確認按鈕每按一次就執行一次guess該方法
         * 1.不回傳資料
         * 2.?: 0 (Elvis運算子)說明 : 1.不希望以後是null 2.如果有值就拿去使用,如果沒有值就給0
         * 3.
         */
    fun guess(num: Int){
        var n = counter.value ?: 0
        n++
        counter.value = n
    }
}