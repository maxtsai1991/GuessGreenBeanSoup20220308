package com.max.guess.data

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room 單元 : 儲存猜數字紀錄 ,
 * Room的設計基本三樣要件 , 就可以使用了 :
 * 1. Entity (類別,Record.kt)
 * 2. Dao (介面,RecordDao.kt) : 存取Record的介面
 * 3. Database (抽象類別,GameDatabase.kt) : 要讓使用者,可以呼叫得到資料庫的一個抽象類別
 *
 * class Record 類別說明:
 * 1. 最後要儲存到資料庫的物件
 * 2. 使用Record(){}方法來儲存資料
 * */
// 範例一 會用在猜數字範例上
@Entity                                 // @Entity 實體的意思, 標示語法
class Record (                          /**在Kotlin可以把建構子跟屬性合併的設計在類別宣告裡面*/
    @NonNull                            // 標示不可以為null空值 , nickname不能為空值
    @ColumnInfo(name="nick")            // 標示nickname名稱使用 , 用途在於覺得名稱太長時可用此標示
    var nickname : String,                // 暱稱
    @NonNull
    var counter : Int                     // 猜了幾次
    ) {
    @PrimaryKey(autoGenerate = true)    // @PrimaryKey 為主鍵值的id標示 , (autoGenerate = true) 為自動新增 , id 不能重複,流水號
    var id : Long = 0
}

// 範例二 目前不會使用
@Entity
class Word {
    @PrimaryKey
    var name : String = ""
    var means : String = ""
    var star : Int = 0
}