package com.max.guess

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.max.guess.databinding.ActivityViewBindingBinding

/**
 * ViewBinding 取代 findViewById 參考 : https://ke.qq.com/course/4128266/12397070916713994
 * 範例說明 : 創建Activity (ViewBindingActivity.kt) & Fragment(FragOne.kt) 頁面 ,並且使用ViewBinding的綁定畫面寫法及存取設定頁面的元件
 * 補充:
 *      如果您希望在生成綁定類時忽略某個佈局文件，請將 tools:viewBindingIgnore="true" 屬性添加到相應佈局文件的根視圖中 EX :
 *      <LinearLayout
            ...
            tools:viewBindingIgnore="true" >
            ...
 *      </LinearLayout>
 *
 *
 */

class ViewBindingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_view_binding)

        /**
         * 使用ViewBinding的綁定畫面寫法
         */
        val binding : ActivityViewBindingBinding = ActivityViewBindingBinding.inflate(layoutInflater)
        val rootView = binding.root
        setContentView(rootView)

        /**
         * 當綁定後,即可使用binding去存取使用該頁面的元件
         */
        binding.btnAction.setOnClickListener {
            Toast.makeText(this, "修改文字成功!", Toast.LENGTH_SHORT).show()
            binding.tvTitle.text = "使用ViewBinding設定文字"
        }


    }
}