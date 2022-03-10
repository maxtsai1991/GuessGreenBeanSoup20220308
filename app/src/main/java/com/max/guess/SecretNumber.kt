package com.max.guess

import java.util.*

class SecretNumber {
    /*
        設計秘密數字類別(猜數字類別)
        1.產生一個範圍是1-10亂數當祕密數字
        2.計數器
        3.validate方法 : 讓人家傳入一個猜測的數字(number: Int) ,跟我現在秘密數字的資料比對
     */
    var secret: Int = Random().nextInt(10) + 1  //1.產生一個範圍是1-10亂數當祕密數字 , 因為有reset方法 需重新產生此方法 ,所以要將val 改成 var , var可以改變的資料
    var count = 0 // 2.計算使用者猜的次數(計數器)

    fun validate(number: Int) : Int{ // 3.比對數字是否相符 ,該方法會回傳Int的值 , 帶入一個number參數,該參數為Int整數
        count++                      // 2.每猜一次 就加1
        return number - secret       // 傳入的值 減掉 亂數當祕密數字 如果為負數 代表 傳入的數字比較小,亂數秘密數字比傳入的數字還大
    }

    fun reset(){
        secret = Random().nextInt(10) + 1       // reset方法 需重新產生此方法 ,所以要將val 改成 var , var可以改變的資料
        count = 0                                      // 將猜的次數 歸0
    }
}

fun main() {
    val secretNumber = SecretNumber()               // new 出SecretNumber物件
    println("亂數產生數字為:${secretNumber.secret}")  // 使用SecretNumber物件下的secret方法 ,該方法是亂數產生數字,範圍是1-10
    println("猜的數字為2,與亂數相差值為: ${secretNumber.validate(2)}")       // 傳入一個2數字到該方法,而如果secret亂數產生數字是7 會得到-5 ,如果得到的數字小於0 就請使用者再猜大一點,反之大於0,就猜小一點
    print("猜的數字為2(小於0就猜大一點,大於0就猜小一點): ${secretNumber.validate(2)},已猜的次數(count): ${secretNumber.count}")

}