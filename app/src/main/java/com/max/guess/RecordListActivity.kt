package com.max.guess

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.max.guess.data.GameDatabase
import kotlinx.android.synthetic.main.activity_record_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class RecordListActivity : AppCompatActivity() , CoroutineScope{
    private lateinit var job: Job // 晚一點才會初始化的屬性
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    /**
     * 此頁面為Record list(遊戲紀錄清單)
     * 該頁的Layout : activity_record_list.xml
     * 該頁的Adapter: RecordAdapter.kt
     * 該頁的ViewHolder : RecordAdapter.kt
     * 該頁的item Layout : row_record.xml
     * 為什麼獨立出adapter: 因為這個紀錄清單,他可能是一個到處都可以用的清單
     */

    /**
     * Coroutines (該頁使用到CoroutineScope) 協程說明:
     *      在目前的執行緒裡面,同時間可以生出輕量的一個方法,新版的Room : 2.1.0 已經可以支援Coroutines的實作
     *      1.   CoroutineScope(吃CoroutineContext,要告訴他你有什麼樣的情境)
     *      1-2. CoroutineContext 官方已經定義好了,使用Dispatchers這個類別,這類別當中定義了幾個常見常用的方法(EX:Dispatchers.IO、Dispatchers.Default、Dispatchers.Main)
     *      1-3. 而因為要使用比較耗時的工作,所以選擇Dispatchers.IO
     *
     * this@RecordListActivity 說明:
     *      原先打this 要修改成 this@RecordListActivity
     *      原因:原this代表CoroutineScope,而這邊this則是指RecordListActivity
     */

    /**
     * 8-3 Coroutines在Android生命週期的活用 (使用情境看第9點)筆記:
     * 好的寫法: 在MainThread(主執行緒)裡面去生出另外一個協程,因為Room本身已經支援協程,這樣子我們就可以在協程之中完成事情
     * 步驟:
     * 1. 在繼承語法後面,在實作一個介面(class RecordListActivity : AppCompatActivity() , CoroutineScope{ }),這個介面就是CoroutineScope介面
     * 2. 這個介面必須要實作一個方法(燈泡熱鍵alt+enter > 按下Implement members)
     * 3. 選擇 coroutineContext: CoroutineContext
     * 4. 即會產生  override val coroutineContext: CoroutineContext ; get() = TODO
     * 5. 在該類別中加一個屬性  private lateinit var job: Job (晚一點才會初始化的job屬性) , Job就是使用Kotlin的類別(kotlinx.coroutines)
     * 6. 在onCreate裡面,必須要去初始化job,產生一個工作 EX : job = Job()
     * 7. 這個工作我們在CoroutineContext物件上面,添加 job + Dispatchers.Main ,並且使用Main的執行緒 EX : get() = job + Dispatchers.Main
     * 8. 做完這些設計後,在onCreate的job就會生成出來
     * 9. 假設我們在遊戲紀錄清單進入該畫面,需要2-3秒以上,可是使用者馬上按下返回,這個時候工作要不要一起把它取消掉,因為我已經離開這個Activity(RecordListActivity),所以當進入到這個畫面時候,耗時工作在執行,如果馬上被按下返回,這個工會也會一併被取消
     * 10.覆寫生命週期裡面的onDestroy方法(快捷鍵:Ctrl+o),在這個方法裡面,要離開之前呼叫job裡面的cancel()方法
     * 11.記得要在Room的Dao的屬性加上suspend (因為+上suspend關鍵字之後,就可以在Coroutines去執行) EX : suspend fun getAll() : List<Record>
     * 12.在onCreate使用launch語法,可以使用launch原因,因launch屬於CoroutineScope介面裡面的一個產生的一個Builder方式 EX :  launch { }
     * 13.在launch裡面,直接取得records EX: val records = GameDatabase.getInstance(this@RecordListActivity)?.recordDao()?.getAll()
     * 14.records得到以後,在直接設定到RecyclerView身上
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_list)
        job = Job() // 初始化job

        launch {
            //取得 records
            val records = GameDatabase.getInstance(this@RecordListActivity)?.
            recordDao()?.getAll()
            /**
             * let?.{ }區塊設計說明 :
             *      假如records是null 區塊內就不會執行
             *      假如records不是null,他就會把recordsr傳進來,叫做it,就會執行區塊內的程式碼
             *      這個設計 主要讓整個程式碼更清楚更明瞭
             * runOnUiThread{ }說明 (在8-3章節刪除了):
             * 代表說區塊裡面的程式碼會配置在UI執行緒裡面執行
             */
            records?.let {
                    recycler.layoutManager = LinearLayoutManager(this@RecordListActivity)  // 注意 這裡的recycler是Layout檔(activity_record_list.xml) RecyclerView ID
                    recycler.setHasFixedSize(true) // 設定固定大小
                    recycler.adapter = RecordAdapter(it) // 設定adapter ; 注意1 : RecordAdapter裡面必須要有一個List集合, 注意2 : 這裡寫it是因為.let{ } 括號裡面it代表 Record的List資料
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}