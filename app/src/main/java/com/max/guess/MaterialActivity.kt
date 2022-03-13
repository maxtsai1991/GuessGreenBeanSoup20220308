package com.max.guess

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.max.guess.databinding.ActivityMaterialBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.ed_number
import kotlinx.android.synthetic.main.content_material.*

class MaterialActivity : AppCompatActivity() {
    private lateinit var viewModel: GuessViewModel                               // 加lateinit原因:這個屬性會晚一點把它生出來,再給初始值,這樣子就不需在類別後面加上 "?" 或 等於null
//    val secretNumber = SecretNumber()
    val TAG = MaterialActivity::class.java.simpleName                            // 等於 val TAG = "MainActivity"寫法 ,把"MainActivity"字串抽取出來 好處是 避免在使用Log.d , 把字打錯 ,在Logcat 搜尋一樣搜尋MainActivity ,而不是TAG

///////
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMaterialBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
         *
         */
        viewModel = ViewModelProvider(this).get(GuessViewModel::class.java)

        /**
         * viewModel.counter.observe()的說明:
         * 1.從viewModel裡面得到counter,因為viewModel會取得GuessViewModel物件
         * 2.呼叫.observe()觀察者模式方法,owner可以放Activity(this),Observer(注意是大寫的O)是介面要實作他身上的方法,未來LiveData改變的時候,就會執行他身體上的方法
         * 3.Observer方法內預設是it:Int! 意思是counter值預設是it,如果不喜歡預設it可以自己取名,這邊取名叫data名稱
         * 4. counter.setText(data.toString()) 的counter是Layout裡面的counter,不是資料的counter,呼叫.setText()方法,把上面的資料(data),因為data是Int型態,要轉型成String,才能放到.setText()方法裡
         */
        viewModel.counter.observe(this, Observer { data ->
            counter.setText(data.toString())
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
//            /* 改使用Mvvm方式,則不須使用傳統寫法*/
            viewModel.reset()
            counter.setText(viewModel.count.toString())
            ed_number.setText(" ")

//            /* 註解下面是因為改使用Mvvm方式,則不須使用傳統寫法*/
//            AlertDialog.Builder(this)
//                .setTitle("重新玩 (Replay Game) !!!")
//                .setMessage("確定嗎  (Are you sure) ?")
//                .setPositiveButton(getString(R.string.ok), {dialog,which ->                        // 確定, 當點擊確定代表要重玩, 監聽器用lambda表示 , lambda表示用 一對大括號{} , 大括號裡面第一個參數用dialog ,第二個參數叫你按了哪一種按鈕(which) , 接著用lambda(->) ,接重置方法
////                    secretNumber.reset()                                       //【因為改成MVVM架構所以不須這行】,改寫成viewModel.reset()
//                    viewModel.reset()
//                   counter.setText(secretNumber.count.toString())               //【因為改成MVVM架構所以不須這行】, 重玩方法執行完後,在將0 setText回去
//                    ed_number.setText("")                                        // 重玩後把輸入框先前輸入的清掉
//                })
//                .setNeutralButton(getString(R.string.Cancel),null)        // 取消 , 目前還未對取消做監聽器處理,所以按了會沒反應
//                .show()
        }
//        counter.setText(secretNumber.count.toString())                           // counter 是已猜次數TextView 的 ID
//        Log.d(TAG,"秘密數字(secret) : " + secretNumber.secret)
    }
///////

    fun check(view : View){                                                       // 確認(OK)按鈕的方法 , 要跟xml > bt_ok按鈕onClick欄位寫一樣名稱,大小寫都要一樣
        val n = ed_number.text.toString().toInt()
        viewModel.guess(n)                                                        // 呼叫GuessViewModel.kt的guess方法

       /** 這一大段註解是因為改用MVVM架構,所以不需要使用舊有寫法
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
         * Toast & AlertDialog 可擇一使用
         */
        Toast.makeText(this,message, Toast.LENGTH_LONG).show()

        AlertDialog.Builder(this)               // AlertDialog(對話框顯示方式) 要選擇 (androidx.appcompat.app) , 因為這樣新舊手機版本都支援 , .Builder(this) > 產生複雜各種型態物件
            .setTitle(getString(R.string.dialog_title))// 對話框的標題 有用多語化 ,先打字串後,再用燈泡熱鍵即可產生
            .setMessage(message)                       // 對話框的訊息
            .setPositiveButton(getString(R.string.ok),null)  // 對話框的按鈕 , 可在對按鈕做監聽器 , text > 按鈕名稱 ,有用多語化 ,先打字串後,再用燈泡熱鍵即可產生
            .show()                                    // 對話框完成之後,在用show把它顯示出來

        */
    }


}