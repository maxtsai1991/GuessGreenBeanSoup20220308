package com.max.guess

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ViewModelsUseFragmentActivity : AppCompatActivity() {
    /**
     * 該範例邏輯 : 當點擊按鈕加一,而Fragment A & B 都會一起改變,因為Fragment A & Fragment B 共享 TextView(tvCount) 使用 ViewModel & MutableLiveData & observe (觀察者)
     * 參考 : https://ke.qq.com/course/4128266/12397062326779402
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_models_use_fragment)
    }
}