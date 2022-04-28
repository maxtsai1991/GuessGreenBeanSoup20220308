package com.max.guess

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.max.guess.databinding.ActivityDataBindingBinding

/**
 * DataBinding數據綁定 注意事項 :
 * 1.   參考 : https://ke.qq.com/course/4128266/12397075211681290
 * 2.   注意type 是使用Integer不是int 在R.layout.activity_data_binding
 *           <variable
 *              name="list"
 *              type="List&lt;Integer>" />
 * 3.   TextView使用DataBinding要在text標籤使用這種方式 :
 *          1. android:text="@{user.name}"     2.  android:text=@{String.valueOf(user.age)}"
 *          3. android:text="@{name}"          4.  android:text="@{String.valueOf(age)}"
 *          5. android:text="@{map[1]}"        5.  android:text="@{String.valueOf(list[0])}"
 *          而用到String.valueOf()是因為整數要轉成字串
 * 4.   使用DataBinding引入Libs後,在要使用的畫面最上方,按Alt + Enter 點選"Convert to data binding layout"(轉換為數據綁定佈局),會生成<data></data>標籤,在裡面添加<variable標籤,即可對元件使用
 *
 */

class DataBindingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_binding)

        // DataBinding綁定Layout方式一 :
//        val binding = ActivityDataBindingBinding.inflate(layoutInflater)

        // DataBinding綁定Layout方式二 :
        val binding = DataBindingUtil.setContentView<ActivityDataBindingBinding>(this,R.layout.activity_data_binding)

        // DataBinding 設定值
        binding.name = "蔡鎮宇"
        binding.age = 31

        // 透過自定義User類別 DataBinding 設定值
        val user = User("馬克思",31)
        binding.user = user

        // list DataBinding 設定值
        val list = listOf(33,45,64)
        binding.list = list

        // map DataBinding 設定值
        val map = mapOf(0 to "Max" , 1 to "Nico")
        binding.map = map

        val clickHandle = ClickHandle()
        binding.clickhandle = clickHandle

        binding.data = this
    }

    fun onClick(v:View){
            // 假設有多個按鈕時,可以使用when(v?.id){    }
            when(v?.id){
                R.id.bt_click2 ->{
                    Log.e("TAG", "DataBinding 測試按鈕來自DataBindingActivity點擊方法 !")
                }
            }
    }
}