package com.max.guess.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * 設計Database類別重要要件:
 * 1. 一定要是抽象類別
 * 2. 一定要繼承RoomDatabase()類別
 * 3. 身上一定要標示RoomDatabase語法, @Database裏頭必須要有兩樣東西 1.entities 2. version EX : @Database()
 * 4. 裏頭必須有取得Dao的抽象方法 EX : abstract fun recordDao() : RecordDao
 * 5. 實作單一物件化的設計
 * Singleton單例模式(確保同一時間只有一個物件)說明:
 * 1. 用傳統寫法,當同樣時間存取資料庫時,程式會出錯
 */
@Database(entities = arrayOf(Record::class, Word::class), version = 1) // entities = arrayOf(Record::class, Word::class) 意思是你有什麼樣的物件物體要儲存
abstract class GameDatabase : RoomDatabase(){  //繼承RoomDatabase()類別 必須給建構子 , 可以給空建構子

    abstract fun recordDao() : RecordDao       // 設計取得Dao的方法 ,方法名稱盡量取成跟Dao類別(RecordDao.kt)一樣的方法名稱

    companion object{                          // companion object{ } 類似Java static靜態設計 , 寫在裡面都是類別層級的
        private var instance : GameDatabase? = null // instance 代表的是GameDatabase的物件, 一開始有可能是null ,所以在定義他的時候要加"?" , 並且給初始值null
        /**
         * 單例模式,開放的方法 :
         * 1. 該方法()括號裡面就是對方呼叫我的時候,要傳進來的參數(context物件), 呼叫此方法會得到GameDatabase物件
         * 2. 檢查是不是null ,如果是null,就把instance產生出來(Room.databaseBuilder(context,GameDatabase::class.java,"game.db").build())
         * 3. Room.databaseBuilder( 別人傳進來的context , 該類別 , 自訂義檔名)
         * 此範例說明: 給我一個context,那我就給你一個資料庫8
         */
        fun getInstance(context: Context) : GameDatabase? {
            if( instance == null ){
                instance = Room.databaseBuilder(context,
                    GameDatabase::class.java,
                        "game.db").build()
            }
            return instance
        }
    }

}