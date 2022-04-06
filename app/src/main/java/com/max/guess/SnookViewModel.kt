package com.max.guess

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.max.guess.data.Event
import com.max.guess.data.EventResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL

/**
 * 9-6 在MVVM架構中使用Coroutines進行網路連線 筆記:
 * 為何在該章節使用MVVM?
 * A: 在Activity裡面使用Coroutines協程是OK的,但如是在讀取比較耗時工作,必須要Activity結束的時候,去把它之前所產生沒有完成的工作把它刪除掉,或是把它取消(停止它),像這樣的程式是比較頭痛的,
 * 所以使用MVVM,使用的ViewModel & LiveData,他們就可以自動的解決這些問題
 * 1. 導入ViewModel & LiveData 類別庫 (可參考官方網站 : https://developer.android.com/jetpack/androidx/releases/lifecycle#groovy)
 *    EX :
 *    def lifecycle_version = "2.5.0-alpha05"
 *    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")   // ViewModel
 *    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")    // LiveData
 *    kapt("androidx.lifecycle:lifecycle-compiler:$lifecycle_version")                  // Annotation processor (標示語法的類別庫)
 * 2. 新增SnookViewModel.kt , 繼承ViewModel() EX : class SnookViewModel : ViewModel(){ }
 * 3. 需要有個LiveData EX : val events = MutableLiveData<List<Event>>() // LiveData放入List集合裡面有Event資料
 * 4. 新增初始化 EX : init { } ,並初始化裡面連線網路及解析(必須要在Coroutines裡面執行),
 *    注意事項:必須在launch建構子使用Dispatchers.IO ,不然會報NetworkOnMainThreadException,因為網路連線不能在主執行緒或是UI執行緒做事
 * 5. 在SnookerActivity.kt 打上 Coroutines協程 第三種寫法(使用MVVM寫法) 程式碼
 */
class SnookViewModel : ViewModel(){
    // LiveData放入List集合裡面有Event資料 , Event資料(指的是API資料,老師使用Snook賽事資料, API網址: http://api.snooker.org/?t=5&s=2021)
    // 未來如果有人傾聽observe觀察這個events,那它裡面的資料只要一改變,就會得到資料
    val events = MutableLiveData<List<Event>>()

    // 初始化程式碼裡面,在連線網路,在解析Json格式,它必須要在Coroutines裡面執行,現在使用的是viewModel裡面有一個Coroutines的Scope,來呼叫launch{ }這個方法,然後就可以進行網路連線的工作
    init {
        viewModelScope.launch(Dispatchers.IO) {
            val data = URL("https://cloud.culture.tw/frontsite/trans/SearchShowAction.do?method=doFindTypeJ&category=6").readText()
            events.postValue(Gson().fromJson(data, EventResult::class.java))
        }
    }
}