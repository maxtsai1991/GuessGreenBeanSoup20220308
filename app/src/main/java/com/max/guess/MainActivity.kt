package com.max.guess

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() { // : AppCompatActivity() 繼承的意思
    override fun onCreate(savedInstanceState: Bundle?) { // 這個onCreate方法允許被子類別所複寫的
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        R.drawable.pig
        R.string.ok
        R.color.messagecolor
    }
}