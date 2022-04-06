package com.max.guess

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import com.max.guess.data.EventResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.URL
import kotlin.coroutines.CoroutineContext

/**
 *  9-5 在Activity中使用Coroutines進行網路連線 筆記 :
 *  1. 添加新的空Activity (此Activity是網路API,Json格式)EX : SnookerActivity.kt
 *  2. 在RecyclerViewMainActivity.kt , functions集合裡,添加"Snooker(網路API)"
 *  3. 在RecyclerViewMainActivity.kt , position 添加 5 -> startActivity(Intent(this,SnookerActivity::class.java))
 *  4. 2跟3是為了在主頁的RecyclerView 添加進入SnookerActivity.kt的選項
 *  5. 撰寫SnookerActivity.kt的程式碼
 *  6. SnookerActivity.kt 的Coroutines協程 寫法一跟寫法二 是一樣的
 */
class SnookerActivity : AppCompatActivity() , CoroutineScope{
    companion object{
        val TAG = SnookerActivity::class.java.simpleName
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_snooker)

        /**
         * Coroutines協程 第二種寫法:
         * 1. 添加CoroutineScope介面 EX : class SnookerActivity : AppCompatActivity() , CoroutineScope{ }
         * 2. 覆寫介面方法 EX : override val coroutineContext: CoroutineContext ; get() = Job()+Dispatchers.IO
         * 3. 就能在onCreate直接使用 launch { 程式碼 } 方法
         */
        launch {
            val data = URL("https://cloud.culture.tw/frontsite/trans/SearchShowAction.do?method=doFindTypeJ&category=6").readText()
            val events = Gson().fromJson(data, EventResult::class.java)
            events.forEach {
                Log.d(TAG, "SnookerActivity_onCreate: $it");
            }
        }

        /**
         * Coroutines協程 第一種寫法:
         */
//        CoroutineScope(Dispatchers.IO).launch {
//            val data = URL("https://cloud.culture.tw/frontsite/trans/SearchShowAction.do?method=doFindTypeJ&category=6").readText()
//            val events = Gson().fromJson(data, EventResult::class.java)
//            events.forEach {
//                Log.d(TAG, "SnookerActivity_onCreate: $it");
//            }
//        }

    }

    /** CoroutineScope介面,覆寫出來的方法,這樣就能在onCreate裡頭使用launch{}方法 */
    override val coroutineContext: CoroutineContext
        get() = Job()+Dispatchers.IO

}