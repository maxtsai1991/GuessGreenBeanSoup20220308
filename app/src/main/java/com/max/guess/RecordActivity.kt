package com.max.guess

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_record.*

/**
 * ADB除錯工具指令 (此範例是用getSharedPreferences方法寫入資料到模擬器,並用ADB指令查看有無進到檔案裏面):
 * 1.  複製SDK路徑 (左上,File > Project Structure > SDK Location > 複製Android SDK location 路徑) ex: C:\Users\NO812E04\AppData\Local\Android\Sdk
 * 2.  打開Terminal (下方,在Build左邊) , 如打開看到在D槽 ,就先切換到C槽 打 c:
 * 3.  進入到SDK路徑 EX: cd C:\Users\NO812E04\AppData\Local\Android\Sdk
 * 4.  進到platform-tools資料夾 EX : cd platform-tools
 * 5.  執行adb指令連到模擬器當中 EX : adb shell
 * 6.  連到模擬器當中的指令是liunx系統,就可以用liunx指令, 輸入su,如輸入su有問題代表模擬器用到 play Store版本的模擬器 ,可以從右上的AVD Manager查看 ,用自己手機也不行,要使用Target欄位 括號顯示 (Google APIs) EX : Pixel 3 XL API 30 這個模擬器 可以正常使用ADB
 * 7.  進到模擬器該專案資料夾 EX : cd /data/data/com.max.guess
 * 8.  列出目前資料夾狀況(查看com.max.guess資料夾下有哪些資料夾或是檔案) EX : ls
 * 9.  進入shared_prefs資料夾 EX : cd shared_prefs
 * 10. 查看用getSharedPreferences()方法 存的guess.xml檔案有無在裡面
 * 11. 查看文字檔內容 EX : cat guess.xml
 * 12. 離開模擬器 EX : exit
 * 13. 另一種方法 : 右下角 > 點選Device File Explorer 看直接查看模擬器資料 EX : /data/data/com.max.guess/shared_prefs/guess.xml
 */
class RecordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)
        val count = intent.getIntExtra("COUNTER(次數)", -1)       // .getIntExtra("和上一頁自訂義字串標籤要寫一模一樣", 當沒有拿到資料時給予預設值) ,此資料是由MateriaActivity.kt傳遞過來的
        counter.setText(count.toString())                                         //  MateriaActivity.kt上一頁取得的資料(count)轉成字串,貼到TextView上(id:counter)
        /**
         * 儲存監聽器
         * 1. 取得使用者輸入欄位的字串
         * 2. 將使用者輸入的暱稱及計算器次數 存進檔案中
         * 3. 利用SharedPreferences 存到模擬器資料夾 檔名為guess.xml
         * 4.
         */
        bt_save.setOnClickListener { view ->                                      // SAVE監聽器
            val nick = et_nickname.text.toString()                                // .text 像是Java的getText 得到文字資料 ；.toString() 得到他的字串值
            getSharedPreferences("guess", Context.MODE_PRIVATE)             // getSharedPreferences ( 檔案名稱 , 存取權限 ) , 存取權限不用背 , 打全大寫MODE就可以選你要的權限 , Context.MODE_PRIVATE 未來儲存的資料只有我這個APP可以使用
                .edit()                                                           // 編輯器,要使用編輯器才有辦法寫入資料
                .putInt("REC_COUNTER",count)                                      // .putInt( 自訂義資料的儲存名字 , 計數器 ) 編輯器放入次數計數器(count)
                .putString("REC_NICKNAME",nick)                                   // .putString(自訂義資料的儲存名字,暱稱)
                .apply()                                                          // 把資料放完後要儲存 可以呼叫兩種方法 1 .commit() : 在該行之後會把資料寫進去   2 .apply() : 不一定會在該行之後寫進去,會利用技巧在有空的時候寫進去 ; 如再下一行就要讀取他就要用commit()方法,反之則用apply()方法
        }
    }
}