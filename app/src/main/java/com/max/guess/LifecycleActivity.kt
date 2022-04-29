package com.max.guess

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Lifecycle

/**
 * Lifecycle(生命週期)介紹與使用 , 點擊到該頁查看LOG觀察生命週期
 *      官方文件 : https://developer.android.google.cn/topic/libraries/architecture/lifecycle#lco
 *      騰訊課堂參考 : https://ke.qq.com/webcourse/4128266/104283995#taid=12397079506648586&vid=387702292255362821
 *
 *      1.  當模擬器Build起來 :    onCreate > onStarte > onResume
 *      2.  當APP切換到多工處理時 : onPause > onStop
 *      3.  從多工處理回到APP :    onStarte > onResume
 *      4.  將APP滑掉時 :         onDestroy
 *      5.  使用生命週期的好處,像是把需要初始化的程式碼移到MyLifeCycleObserver.kt的onCreate()方法裡,減輕Activity負擔
 */
class LifecycleActivity : AppCompatActivity() {
    private  var state : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lifecycle)

        // 註冊lifecycle 添加觀察者
        lifecycle.addObserver(MyLifeCycleObserver())

        // 判斷當前狀態是否處於CREATED狀態 , 此行程式碼在處理程式邏輯時可以使用
        state = lifecycle.currentState.isAtLeast (Lifecycle.State.CREATED)
    }
}