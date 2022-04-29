package com.max.guess

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.max.guess.databinding.ActivityDataBinding2Binding

/**
 * DataBinding雙向數據綁定 騰訊課程參考 : https://ke.qq.com/webcourse/4128266/104283995#taid=12397083801615882&vid=387702292255353219 , 該範例是點選修改按鈕,改變CheckBox選項,以及點選CheckBox查看Log選項為true or false
 *
 * 1.   activity_data_binding2 Layout的CheckBox 補充說明( 官方參考 : https://developer.android.google.cn/topic/libraries/data-binding/two-way) :
 *          @={}表示法（其中重要的是包含“=”符號）可接收屬性的數據更改並同時監聽用戶更新。
 * 2. 該範例是點選修改按鈕,改變CheckBox選項,以及點選CheckBox查看Log選項為true or false
 */

class DataBindingActivity2 : AppCompatActivity() {
    /**
     * 讓DataBindingActivity2 認得自定義RememberMe類別
     */
    val model : RememberMe by lazy { RememberMe() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_binding2)

        // DataBinding 綁定Layout
        val binding : ActivityDataBinding2Binding =
            DataBindingUtil.setContentView(this,R.layout.activity_data_binding2)

        /**
         *  點選修改按鈕,改變CheckBox選項
         */
        binding.btnChange2.setOnClickListener {
            val isChecked = model.getRememberMe()
            if (isChecked){
                model.setRememberMe(false)
            }else{
                model.setRememberMe(true)
            }
        }
        binding.model = model // 上面程式碼邏輯判斷修改後, 需寫這行,才算是修改到Data
    }
}