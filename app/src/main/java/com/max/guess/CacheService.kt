package com.max.guess

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

/**
 * 10-4 認識服務,Service (Android裏頭的四大元件)
 *補充說明:
 * Service 是一個沒有畫面的元件,它在執行的時候是在背後執行,不像Activity一執行時就要有畫面,Service沒有畫面
 * Service 有兩大類 , 一類是自生自滅,執行完就結束了 , 另一類是跟Activity有相關聯,這種Service叫onBind的Service , 此範例是後者
 * 1.  新增Kotlin檔案 : com.max.guess 右鍵 > New > Kotlin File/Class > 命名: CacheService
 * 2.  繼承Service() (選擇android.app) EX : class CacheService() : Service(){ } , 注意 : 類別名跟繼承都需要有建構子 括號()
 * 3.  覆寫Service實作必要方法(Alt + Enter) > Implement members > 必要方法onBind() > 可以直接先return null
 * 4.  在manifests 幫Service加一個紀錄,加在<application標籤內部,因為是四大元件之一 EX : <service android:name=".CacheService"/>
 * 5.  覆寫三個方法(快捷鍵Ctrl + o , android.app.Service): 1. onStartCommand 2. onCreate 3. onDestroy
 * 5-1.onCreate方法 : Service一旦被系統產生以後,會直接呼叫該方法
 * 5-2.onDestroy方法 : 當APP點選返回,回到模擬器首頁,會呼叫該方法
 * 5-3.onStartCommand方法 : 這個方法return值蠻特別的,假設這個Service執行了,Android系統因為支援短缺,而把Service殺掉的時候,你該怎麼做,打全大寫的STICKY有三種 (START_STICKY(當被系統殺掉時,自己再把它生出來) 、 START_NOT_STICKY(當被系統殺掉時,不會產生回來) 、 START_STICKY_COMPATIBILITY)
 * 6.  添加Log訊息,這樣可以知道何時執行覆寫的三個方法 EX : Log.d(TAG, "onStartCommand : ");
 * 7.  產生Service讓他跑出來,到RecyclerViewMainActivity.kt在選單的onOptionsltemSelected覆寫方法添加程式碼
 * 8.  建立Intent物件 , 這個Intent就是專門把Service叫出來 ( EX : Intent(this,CacheService::class.java)) , 最後再呼叫startService ,把Intent傳進去
 * 點選模擬器返回鍵,沒有跳出onDestroy,是因為沒有被記憶體釋放,避免Service沒有被記憶體釋放的方法
 * 1. 將cacheService變成是屬性,放在類別下屬性位置 , 要晚一點再生出來 , 所以一開始的時候應會是null , 改成 var , 那null的時候就要定義這個cacheServic屬性是Intent型態 , 要加問號? ,它是允許為null的 EX : var cacheService : Intent? = null
 * 2. 再把將把它建立出來 EX : cacheService = Intent(this,CacheService::class.java)
 * 3. 再覆寫onStop方法(快捷鍵Ctrl+O) , 再呼叫stopService()方法,將cacheService傳進去 EX : RecyclerViewMainActivity.kt > override fun onStop() { super.onStop() ;  stopService(cacheService) ; }
 * 測試Service : 在RecyclerViewMainActivity畫面,點選下載圖示 , 然後在Logcat 搜尋Cache
 */

class CacheService() : Service(){
    private val TAG = CacheService::class.java.simpleName

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand : ");
        return START_STICKY
    }

    override fun onCreate() { // Service一旦被系統產生以後,會直接呼叫onCreate
        super.onCreate()
        Log.d(TAG, "onCreate : ");
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy : ");
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}