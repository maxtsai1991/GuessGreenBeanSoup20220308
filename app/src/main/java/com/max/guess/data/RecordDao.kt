package com.max.guess.data

import androidx.room.*

@Dao                                                 // Room的專用標識語法
interface RecordDao {                                // 設計Dao 用interface介面

    @Insert(onConflict = OnConflictStrategy.REPLACE)  // 新增 : (onConflict = OnConflictStrategy.REPLACE) 意思是當有重複資料新增進來時,就更新同筆資料
    suspend fun insert(record : Record)                       // 設計Dao,新增資料

    @Query("select * from record")              // 查詢 : Room 查詢標示語法
    suspend fun getAll() : List<Record>               // 設計Dao,取得全部得紀錄 , 不用給任何參數, 會回傳list集合,裡面包含了許多Record資料 ; +上suspend關鍵字之後,getAll()就可以在Coroutines去執行

    @Delete
    suspend fun delete(record: Record)
}