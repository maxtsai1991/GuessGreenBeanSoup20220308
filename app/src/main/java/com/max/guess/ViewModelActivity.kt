package com.max.guess

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels

class ViewModelActivity : AppCompatActivity(), View.OnClickListener {

    /**
     * 點擊按鈕,數字加一
     * 監聽器使用介面放在類別
     */

    private lateinit var tvNum: TextView
    private lateinit var btnAdd: Button
    private val model:MyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_model)

        tvNum = findViewById(R.id.tv_num)
        btnAdd = findViewById(R.id.btn_add)
        btnAdd.setOnClickListener(this)      // 按鈕監聽器,搭配監聽器使用介面放在類別
        tvNum.text = model.number.toString()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_add ->{
                var num = model.number
                num++
                tvNum.text = num.toString()
                model.number = num
            }
        }
    }

}