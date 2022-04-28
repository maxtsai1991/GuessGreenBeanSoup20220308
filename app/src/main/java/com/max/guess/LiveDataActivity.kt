package com.max.guess

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import kotlin.concurrent.thread


/**
 * 當點擊後改變數字
 * 當點擊按鈕會改變數字,及會改變TextView(tvName) 因為 : 更新到model裡的nameLd值(updateName()方法),
 * 而 model.nameLd使用observe(觀察者方法)就會改變的值給予TextView(tvName.text)
 *
 * 知識點 (參考 : https://developer.android.google.cn/topic/libraries/architecture/livedata#create_livedata_objects):
 * 1.   LiveData是一種可觀察的數據存儲器類。與常規的可觀察類不同，LiveData 具有生命週期感知能力，意指它遵循其他應用組件（如Activity、Fragment 或Service）的生命週期。
 * 2.   如果觀察者（由 Observer 類表示）的生命週期處於 STARTED 或 RESUMED 狀態，則LiveData 會認為該觀察者處於活躍狀態。
 * 3.   LiveData 只會將更新通知給活躍的觀察者。為觀察 LiveData 對象而註冊的非活躍觀察者不會收到更改通知。
 * 4.   當相應的 Lifecycle 對象的狀態變為 DESTROYED 時，便可移除此觀察者。這對於Activity 和Fragment 特別有用，因為它們可以放心地觀察 LiveData 對象，而不必擔心洩露（當Activity 和Fragment 的生命週期被銷毀時，系統會立即退訂它們）。
 * 5.   LiveData 與Room 一起使用 :
 *      Room持久性庫支持返回 LiveData 對象的可觀察查詢。可觀察查詢屬於數據庫訪問對象(DAO) 的一部分。
 * 6.   協程與LiveData 一起使用 :
 *      使用 LiveData 時，您可能需要異步計算值。例如，您可能需要檢索用戶的偏好設置並將其傳送給界面。在這些情況下，您可以使用 liveData 構建器函數調用 suspend 函數，並將結果作為 LiveData 對像傳送。
 */
class LiveDataActivity : AppCompatActivity() {
    val TAG = LiveDataActivity::class.java.simpleName
    private lateinit var tvName: TextView
    private lateinit var btnChange: Button
    private val model : LiveDataViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_data)

        tvName = findViewById(R.id.tv_name)
        btnChange = findViewById(R.id.btn_change)
        btnChange.setOnClickListener {
            Log.e(TAG,"click")

            var changeVal = (0..100).random()
            var str = "LiveData範例,當點擊時給予0到100的亂數 : $changeVal"
            tvName.text = str

            thread {
//                model.updateName(str)         // 該行只限於在UI主執行緒中使用,所以先註解掉,如要使用移出thread{}外即可; 當點擊後改變數字的方法, 寫這行才會改變LiveDataViewModel裡的nameLd.value
                model.updataNameThread(str)     // 該行可以在子執行緒使用 , 主要是updataNameThread()方法 , 使用postValue()方法
            }

        }

        model.nameLd.observe(this){
            tvName.text = it
        }

    }


}