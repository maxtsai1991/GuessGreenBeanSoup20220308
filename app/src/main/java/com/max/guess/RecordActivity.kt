package com.max.guess

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_record.*

class RecordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)
        val count = intent.getIntExtra("COUNTER(次數)", -1)       // .getIntExtra("和上一頁自訂義字串標籤要寫一模一樣", 當沒有拿到資料時給予預設值) ,此資料是由MateriaActivity.kt傳遞過來的
        counter.setText(count.toString())
    }
}