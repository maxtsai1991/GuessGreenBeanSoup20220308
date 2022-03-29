package com.max.guess

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.max.guess.data.GameDatabase
import kotlinx.android.synthetic.main.activity_record_list.*

class RecordListActivity : AppCompatActivity() {
    /**
     * 此頁面為Record list(遊戲紀錄清單)
     * 該頁的Layout : activity_record_list.xml
     * 該頁的Adapter: RecordAdapter.kt
     * 該頁的ViewHolder : RecordAdapter.kt
     * 該頁的item Layout : row_record.xml
     * 為什麼獨立出adapter: 因為這個紀錄清單,他可能是一個到處都可以用的清單
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_list)

        AsyncTask.execute { /**另開一個執行緒 EX : AsyncTask.execute { }*/
            //取得 records
            val records = GameDatabase.getInstance(this)?.recordDao()?.getAll()

            /**
             * let?.{ }區塊設計說明 :
             *      假如records是null 區塊內就不會執行
             *      假如records不是null,他就會把recordsr傳進來,叫做it,就會執行區塊內的程式碼
             *      這個設計 主要讓整個程式碼更清楚更明瞭
             * runOnUiThread{ }說明 :
             * 代表說區塊裡面的程式碼會配置在UI執行緒裡面執行
             */
            records?.let {
                runOnUiThread {
                    recycler.layoutManager = LinearLayoutManager(this)  // 注意 這裡的recycler是Layout檔(activity_record_list.xml) RecyclerView ID
                    recycler.setHasFixedSize(true) // 設定固定大小
                    recycler.adapter = RecordAdapter(it) // 設定adapter ; 注意1 : RecordAdapter裡面必須要有一個List集合, 注意2 : 這裡寫it是因為.let{ } 括號裡面it代表 Record的List資料
                }
            }
        }
    }
}