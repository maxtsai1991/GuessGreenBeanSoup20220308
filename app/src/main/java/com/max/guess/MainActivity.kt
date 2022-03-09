package com.max.guess

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
    /*
     猜數字遊戲
     */
class MainActivity : AppCompatActivity() { // : AppCompatActivity() 繼承的意思
    val secretNumber = SecretNumber()
    override fun onCreate(savedInstanceState: Bundle?) { // 這個onCreate方法允許被子類別所複寫的
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun check(view : View){                             // ok按鈕的方法 , 要跟xml > bt_ok按鈕onClick欄位寫一樣名稱,大小寫都要一樣
        val n = ed_number.text.toString().toInt()       // ed_number是文字輸入方塊的ID , .text是取得文字輸入方塊的類別(Editable類別) , .toString() : 取得文字 , .toInt() : 轉成整數 , 再將取得的存到val n
        println("number: $n")
        Log.d("MaintActivity","number:" + n)
    }


}