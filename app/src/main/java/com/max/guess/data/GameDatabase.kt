package com.max.guess.data

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * 設計Database類別重要要件:
 * 1. 一定要是抽象類別
 * 2. 一定要繼承RoomDatabase()類別
 * 3. 身上一定要標示RoomDatabase語法, @Database裏頭必須要有兩樣東西 1.entities 2. version EX : @Database()
 * 4. 裏頭必須有取得Dao的抽象方法 EX : abstract fun recordDao() : RecordDao
 * 5. 實作單一物件化的設計
 */
@Database(entities = arrayOf(Record::class, Word::class), version = 1) // entities = arrayOf(Record::class, Word::class) 意思是你有什麼樣的物件物體要儲存
abstract class GameDatabase : RoomDatabase(){  //繼承RoomDatabase()類別 必須給建構子 , 可以給空建構子

    abstract fun recordDao() : RecordDao       // 設計取得Dao的方法 ,方法名稱盡量取成跟Dao類別(RecordDao.kt)一樣的方法名稱
}