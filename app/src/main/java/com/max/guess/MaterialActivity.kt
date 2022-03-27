package com.max.guess

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.room.Room
import com.max.guess.data.GameDatabase
import com.max.guess.data.Record
import com.max.guess.databinding.ActivityMaterialBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.ed_number
import kotlinx.android.synthetic.main.content_material.*

/**
 * 複寫方法快捷鍵 ctrl + o
 * 將程式碼抽出去變獨立方法快捷鍵 ctrl + alt + m
 */
class MaterialActivity : AppCompatActivity() {
    private val REQUEST_RECORD = 100
    private lateinit var viewModel: GuessViewModel                               // 加lateinit原因:這個屬性會晚一點把它生出來,再給初始值,這樣子就不需在類別後面加上 "?" 或 等於null
    val secretNumber = SecretNumber()                                            // Kotlin 出 SecretNumber物件
    val TAG = MaterialActivity::class.java.simpleName                            // 等於 val TAG = "MainActivity"寫法 ,把"MainActivity"字串抽取出來 好處是 避免在使用Log.d , 把字打錯 ,在Logcat 搜尋一樣搜尋MainActivity ,而不是TAG

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMaterialBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ");
        binding = ActivityMaterialBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        /**
         * viewModel物件的取得
         * 1.  定一個變數 val viewModel ,命名變數注意事項,第一個字小寫後面的單字大寫
         * 2.  ViewModelProvider類別,建構子要的是ViewModelStoreOwner,可以用Activity,直接寫this
         * 3.  呼叫.get方法 : 告訴我類別的名稱,我幫你把它生出來,再交給你這個ViewModel物件 EX: .get(GuessViewModel::class.java)
         * 4.  記得給get方法的類別用 ::class.java
         * 5.  從區域變數改成全域屬性(變數)
         * 5-1.將原本 val viewModel 刪掉val ,然後用燈泡熱鍵,選擇Create property 'viewModel' (產生一個viewModel屬性)
         * 5-2.之後會有紅線毛毛蟲,是因為他不會再一開始就產生,是經過後續的onCreate之後才會有,處理方式:燈泡熱鍵,選擇Add 'lateinit' Modifier (這個屬性會晚一點把它生出來,再給初始值,這樣子就不需在類別後面加上 "?" 或 等於null)
         */
        viewModel = ViewModelProvider(this).get(GuessViewModel::class.java)

        /**
         * viewModel.counter.observe()的說明:
         * 1.從viewModel裡面得到counter,因為viewModel會取得GuessViewModel物件
         * 2.呼叫.observe()觀察者模式方法,owner可以放Activity(this),Observer(注意是大寫的O)是介面要實作他身上的方法,未來LiveData改變的時候,就會執行他身體上的方法
         * 3.Observer方法內預設是it:Int! 意思是counter值預設是it,如果不喜歡預設it可以自己取名,這邊取名叫data名稱
         * 4. counter.setText(data.toString()) 的counter是Layout裡面的counter,不是資料的counter,呼叫.setText()方法,把上面的資料(data),因為data是Int型態,要轉型成String,才能放到.setText()方法裡
         * 程式流程備忘錄:
         */
        viewModel.counter.observe(this, Observer { data ->
            counter.setText(data.toString())                    //這裡的counter是layout 猜次數的Textview id , data是我們上一行自己取名的名稱
        })

        viewModel.result.observe(this, Observer { result ->
            var message = when(result){
                GameResult.BIGGER ->        "猜大一點(Bigger)"
                GameResult.SMALLER ->       "猜小一點(Smaller)"
                GameResult.NUMBER_RIGHT ->  "賓果,你答對了(Yes! you got it)"
            }
            AlertDialog.Builder(this)               // AlertDialog(對話框顯示方式) 要選擇 (androidx.appcompat.app) , 因為這樣新舊手機版本都支援 , .Builder(this) > 產生複雜各種型態物件
                .setTitle(getString(R.string.dialog_title))// 對話框的標題 有用多語化 ,先打字串後,再用燈泡熱鍵即可產生
                .setMessage(message)                       // 對話框的訊息
                .setPositiveButton(getString(R.string.ok),null)  // 對話框的按鈕 , 可在對按鈕做監聽器 , text > 按鈕名稱 ,有用多語化 ,先打字串後,再用燈泡熱鍵即可產生
                .show()                                    // 對話框完成之後,在用show把它顯示出來
        })

        /**
         * fab 是右下角的重完按鈕的id
         */
        binding.fab.setOnClickListener { view ->
            /* 改使用Mvvm方式,則不須使用傳統寫法*/
            viewModel.reset()
            counter.setText(viewModel.count.toString())
            ed_number.setText(" ")

        /* 傳統寫法,如使用Mvvm方式,則需註解下面*/
            replay()
        }
        counter.setText(secretNumber.count.toString())                           // counter 是已猜次數TextView 的 ID
        Log.d(TAG,"MaterialActivity_onCreate_Secret(秘密數字): " + secretNumber.secret)

        /**
         * 使用SharedPreferences讀取檔案資料, 可以在logcat查看
         */
        val count = getSharedPreferences("guess",Context.MODE_PRIVATE)      // getSharedPreferences( 檔名 , 權限 ) 檔名要寫得跟RecordActivity.kt寫入頁一樣 , Context.MODE_PRIVATE權限: 代表只有該APP有權限
            .getInt("REC_COUNTER",-1)                                // getInt( 自己命名的計數器名字 , 當取得不到則必須給預設值(-1) ) : 取得資料用get,這裡要取得計數器次數所以用getInt
        val nick = getSharedPreferences("guess",Context.MODE_PRIVATE)
            .getString("REC_NICKNAME",null)                          // getString( 自己命名的暱稱 , 當取得不到則必須給預設值(null) ) : 這裡要取得暱稱所以用getString
        Log.d(TAG, "(data) 計數器次數 : $count / 暱稱 : $nick");

        // Room test
        /**
         * 1. 先得到database
         * 2. Room.databaseBuilder( context , 傳入GameDatabase類別 , 自訂義db名字ex:game.db )
         * 3. database生成之後,必須呼叫.build()方法,才可以產生出你要的GameDatabase物件
         * 4. 建立產生測試用紀錄
         * 5. 不能用UI Main 主執行緒會報錯, 要用子執行緒 EX :  Thread(){ database.recordDao().insert(record) } ; 報錯訊息: 無法訪問主線程上的數據庫，因為它可能會長時間鎖定 ui (cannot access database on the main thread since it may potentially lock the ui for a long period of time.)
         */
        /**因使用單例模式,所以註解掉*/
//        val database = Room.databaseBuilder(this,       // 從Room類別,身上有一個方法(databaseBuilder),利用此方法產生出一個物件,這個物件就是GameDatabase物件
//        GameDatabase::class.java,"game.db")
//            .build()
//        val record = Record("Maxx",3)          // 產生測試用記錄,每次模擬器重build會在game.db資料庫建立一筆暱稱為Maxx猜測次數為3及流水號的資料 EX : Maxx|3|1 ,可用adb指令在Terminal查詢
//        Thread(){
//            database.recordDao().insert(record)
//        }.start()                                               // .start()方法:執行該執行緒

        /**單例模式*/
        // Room read test
        AsyncTask.execute {
            val list = GameDatabase.getInstance(this)?.recordDao()?.getAll()
            list?.forEach {
                Log.d(TAG, "record: ${it.nickname} ${it.counter}");
            }
        }

    }

    private fun replay() {
        AlertDialog.Builder(this)
            .setTitle("重新玩 (Replay Game) !!!")
            .setMessage("確定嗎  (Are you sure) ?")
            .setPositiveButton(getString(R.string.ok), { dialog, which ->     // 確定, 當點擊確定代表要重玩, 監聽器用lambda表示 , lambda表示用 一對大括號{} , 大括號裡面第一個參數用dialog ,第二個參數叫你按了哪一種按鈕(which) , 接著用lambda(->) ,接重置方法
                    secretNumber.reset()                                       //【因為改成MVVM架構所以不須這行】,改寫成viewModel.reset()
                    viewModel.reset()
                    counter.setText(secretNumber.count.toString())               //【因為改成MVVM架構所以不須這行】, 重玩方法執行完後,在將0 setText回去
                    ed_number.setText("")                                        // 重玩後把輸入框先前輸入的清掉
                })
            .setNeutralButton(getString(R.string.Cancel), null)              // 取消 , 目前還未對取消做監聽器處理,所以按了會沒反應
            .show()
    }

    /**
     * 重新build專案到模擬器生命週期:
     * onCreate > onStart > onResume
     * 當猜對數字時,畫面會由MaterialActivity.kt > RecordActivity.kt, 此時的MaterialActivity生命週期:
     * onPause > onStop
     * 當菜對數字後,跳到RecordActivity.kt,再RecordActivity.kt返回到MaterialActivity.kt, 此時的MaterialActivity生命週期:
     * onRestart > onStart > onResume
     * 當不玩猜數字,返回到模擬器首頁,此時的MaterialActivity生命週期:
     * onPause > onStop > onDestroy
     */
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ");
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: ");
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ");
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ");
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart: ");
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ");
    }

    fun check(view : View){                                                       // 確認(OK)按鈕的方法 , 有在content_material.xml的確認按鈕做監聽器(onClick欄位) , 填寫該方法名稱(check) ,ps.大小寫兩邊都要一樣
//        val n = ed_number.text.toString().toInt()
//        viewModel.guess(n)                                                        // 呼叫GuessViewModel.kt的guess方法

       /** 舊有寫法 ,如使用MVVM架構,要註解下面一大段 */
       val n = ed_number.text.toString().toInt()                                 // ed_number是文字輸入方塊的ID , .text是取得文字輸入方塊的類別(Editable類別) , .toString() : 取得文字 , .toInt() : 轉成整數 , 再將取得的存到val n
        println("使用者輸入的數字(number) : $n")                                     // 可在Logcat 搜尋字串 找到n的值 , n = 使用者輸入的數字
        Log.d(TAG,"使用者輸入的數字(number) : " + n)                            // Log出使用者輸入的數字 , Logcat 搜尋MainActivity可得知n

        /**
         * 判斷猜數字,並Toast出提示使用者訊息 兩種寫法
         */
        val diff = secretNumber.validate(n)                                       // 將判斷式抽出來 , 會這樣做是因為 下面if ...else 要判斷三種不童情況(大於,小於, 等於),就不需要secretNumber.validate(n)寫三次

//        // 判斷的寫法一:
//        if( diff < 0){
//            Toast.makeText(this,"再大一點(Bigger)",Toast.LENGTH_LONG).show()       // 小於,當使用者輸入的數字(number) 小於 秘密數字(secret) 代表要在猜大一點
//        }else if(diff > 0){
//            Toast.makeText(this,"再小一點(Smaller)",Toast.LENGTH_LONG).show()      // 大於,當使用者輸入的數字(number) 大於 秘密數字(secret) 代表要在猜小一點
//        }else
//            Toast.makeText(this,"賓果(Yes, you got it.)",Toast.LENGTH_LONG).show()// 等於,當使用者輸入的數字(number) 等於 秘密數字(secret) 代表猜對了

        // 判斷的寫法二(簡潔有力寫法) ,有用多語化 ,先打字串後,再用燈泡熱鍵即可產生:
        var message = getString(R.string.yes_you_got_it) // 賓果
        if( diff < 0){
            message = getString(R.string.Bigger)  // 再大一點
        }else if(diff > 0){
            message = getString(R.string.Smaller) // 再小一點
        }
        counter.setText(secretNumber.count.toString())                              // counter 是已猜次數TextView 的 ID ,這裡要寫這段 才會累加次數

        /**
         * Toast & AlertDialog
         * Toast: 吐司
         * AlertDialog :
         * 1. 對話框的按鈕 , 可在對按鈕做監聽器,將null改寫監聽器動作 EX :  .setPositiveButton(getString(R.string.ok),null) 改成 .setPositiveButton(getString(R.string.ok),{dialog,which -> 寫條件判斷 })
         * 2. 當使用者猜對數字則跳轉到RecordActivity,用Intent類別跳轉 EX: val intent = Intent(this,RecordActivity::class.java)
         * 3. startActivity(intent) 此方法產生出intent
         * 4. intent.putExtra(自訂義字串名字,要傳遞的資料)       傳遞資料到下一頁Activity , 自訂義字串名字 在下一頁取得時 要打一模一樣,才能拿到傳遞的資料
         */
        Toast.makeText(this,message, Toast.LENGTH_LONG).show()
        AlertDialog.Builder(this)                                                // AlertDialog(對話框顯示方式) 要選擇 (androidx.appcompat.app) , 因為這樣新舊手機版本都支援 , .Builder(this) > 產生複雜各種型態物件
            .setTitle(getString(R.string.dialog_title))                                 // 對話框的標題 有用多語化 ,先打字串後,再用燈泡熱鍵即可產生
            .setMessage(message)                                                        // 對話框的訊息
            .setPositiveButton(getString(R.string.ok),{dialog,which ->
                if(diff == 0){                                                          // 當使用者猜對數字則跳轉到RecordActivity,用Intent類別跳轉
                    val intent = Intent(this,RecordActivity::class.java)
                    intent.putExtra("COUNTER(次數)",secretNumber.count)           //  intent.putExtra(自定義的字串標籤,要傳遞過去的值(資料)) 注意事項:自定義標籤 在下一頁RecordActivity取得此資料時,填寫的標籤要寫一模一樣
//                    startActivity(intent)                                             // 註解 startActivity(intent) 改用 startActivityForResult(intent,REQUEST_RECORD)
                    startActivityForResult(intent,REQUEST_RECORD)                       // startActivityForResult()方法,是啟動第二個activity(為了某些結果而啟動activity),可搭配第二個activity(RecordActivity.kt)的setResult()方法使用,要在finish()方法前使用,
                }
            })
            .show()                                                                     // 對話框完成之後,在用show把它顯示出來
    }

    /**
     * 程式邏輯:MaterialActivity.kt 猜對數字後 > 跳到RecordActivity.kt > 輸入暱稱,點選SAVE > 將暱稱帶回 MaterialActivity.kt (logd印出暱稱),並且跳出重玩視窗(replay())
     * 複寫onActivityResult方法的前因後果:
     * 1.使用startActivityForResult()方法 : 為了某些結果而啟動這個activity
     * 2.這個activity啟動後,要在finish之前要設定結果(呼叫setResult()方法),之後在finish
     * 3.注意:必須要在前一個activity(MaterialActivity.kt)身上複寫onActivityResult方法,才可以接收到另外一個activity(RecordActivity.kt)之後所傳回來的資料
     * 4.這樣的實作方式,就可以讓你得到資料
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_RECORD){
            if(resultCode == Activity.RESULT_OK){
                val nickname = data?.getStringExtra("NICK") //因為data不一定會有,所以要使用 "?"
                Log.d(TAG, "onActivityResult: ${nickname}");
                replay()
            }
        }
    }

    /**
     * 5-5章節 測試範例 : 活用Kotlin的apply與also擴充語法,提升程式碼可讀性
     * 1.建立Intent物件,得到有RecordActivity類別的物件 EX : val intent = Intent(this,RecordActivity::class.java)
     * 2.放入資料 EX : intent.putExtra("A","abc")
     * 3.將資料帶到RecordActivity.kt頁面 EX : startActivity(intent)
     */
    fun test(){
        /** java 寫法 */
        val intent = Intent(this,RecordActivity::class.java)
        intent.putExtra("A","abc")
        intent.putExtra("BB","Testing")
        startActivity(intent)
        /** Kotlin 寫法 */
        Intent(this,RecordActivity::class.java).apply {
            putExtra("A","abc")
            putExtra("BB","Testing")
//            startActivity(intent) // 等同於 .also { intent -> startActivity(intent) }
        }.also { intent ->
            startActivity(intent)
        }
    }
}