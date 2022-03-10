package com.max.guess

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
    /*
     猜數字遊戲
     */
class MainActivity : AppCompatActivity() {                                         // : AppCompatActivity() 繼承的意思
    val secretNumber = SecretNumber()
    override fun onCreate(savedInstanceState: Bundle?) {                           // 這個onCreate方法允許被子類別所複寫的
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("MainActivity","秘密數字(secret) : " + secretNumber.secret) // Log出秘密數字(1-10亂數產生的數字 > secretNumber.secret) , Logcat 搜尋MainActivity可得知secretNumber.secret
    }

    fun check(view : View){                                                       // ok按鈕的方法 , 要跟xml > bt_ok按鈕onClick欄位寫一樣名稱,大小寫都要一樣
        val n = ed_number.text.toString().toInt()                                 // ed_number是文字輸入方塊的ID , .text是取得文字輸入方塊的類別(Editable類別) , .toString() : 取得文字 , .toInt() : 轉成整數 , 再將取得的存到val n
        println("使用者輸入的數字(number) : $n")                                     // 可在Logcat 搜尋字串 找到n的值 , n = 使用者輸入的數字
        Log.d("MainActivity","使用者輸入的數字(number) : " + n)            // Log出使用者輸入的數字 , Logcat 搜尋MainActivity可得知n

        /**
         * 判斷猜數字,並Toast出提示使用者訊息 兩種寫法
         */
        val diff = secretNumber.validate(n)                                       // 將判斷式抽出來 , 會這樣做是因為 下面if ...else 要判斷三種不童情況(大於,小於, 等於),就不需要secretNumber.validate(n)寫三次
//        // 判斷的寫法一:
//        if( diff < 0){
//            Toast.makeText(this,"再大一點(Bigger)",Toast.LENGTH_LONG).show()       // 小於,當使用者輸入的數字(number) 小於 秘密數字(secret) 代表要在猜大一點
//        }else if(diff > 0){
//            Toast.makeText(this,"再小一點(Smaller)",Toast.LENGTH_LONG).show()      // 大於,當使用者輸入的數字(number) 大於 秘密數字(secret) 代表要在猜小一點
//        }else
//            Toast.makeText(this,"賓果(Yes, you got it.)",Toast.LENGTH_LONG).show()// 等於,當使用者輸入的數字(number) 等於 秘密數字(secret) 代表猜對了

        // 判斷的寫法二(簡潔有力寫法):
        var message = "賓果(Yes, you got it.)"
        if( diff < 0){
            message = "再大一點(Bigger)"
        }else if(diff > 0){
            message = "再小一點(Smaller)"
        }

        /**
         * Toast & AlertDialog 可擇一使用
         */
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()

        AlertDialog.Builder(this)               // AlertDialog(對話框顯示方式) 要選擇 (androidx.appcompat.app) , 因為這樣新舊手機版本都支援 , .Builder(this) > 產生複雜各種型態物件
            .setTitle("訊息(Message)")                  // 對話框的標題
            .setMessage(message)                       // 對話框的訊息
            .setPositiveButton("ok",null)  // 對話框的按鈕 , 可在對按鈕做監聽器 , text > 按鈕名稱
            .show()                                    // 對話框完成之後,在用show把它顯示出來

    }


}