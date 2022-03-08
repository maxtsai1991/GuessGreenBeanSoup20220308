package com.max.guess

import java.util.*

class SecretNumber {
    /*

     */
    val secret: Int = Random().nextInt(10) + 1  //亂數產生一個祕密數字範圍是1-10
    var count = 0 // 計算使用者猜的次數

    fun validate(number: Int) : Int{ // 比對數字是否相符 ,該方法會回傳Int的值 , 帶入一個number參數
        count++                      // 每猜一次 就加1
        return number - secret
    }
}

fun main() {
    val secretNumber = SecretNumber()               // new 出SecretNumber物件
    println("亂數產生數字為:${secretNumber.secret}")  // 使用SecretNumber物件下的secret方法 ,該方法是亂數產生數字,範圍是1-10
    println("猜的數字為2,與亂數相差值為: ${secretNumber.validate(2)}")       // 傳入一個2數字到該方法,而如果secret亂數產生數字是7 會得到-5 ,如果得到的數字小於0 就請使用者再猜大一點,反之大於0,就猜小一點
    print("猜的數字為2(小於0就猜大一點,大於0就猜小一點): ${secretNumber.validate(2)},已猜的次數(count): ${secretNumber.count}")
}