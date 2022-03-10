package com.max.guess

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.max.guess.databinding.ActivityMaterialBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.ed_number
import kotlinx.android.synthetic.main.content_material.*

class MaterialActivity : AppCompatActivity() {
    val secretNumber = SecretNumber()
    val TAG = MaterialActivity::class.java.simpleName                                  // 等於 val TAG = "MainActivity"寫法 ,把"MainActivity"字串抽取出來 好處是 避免在使用Log.d , 把字打錯 ,在Logcat 搜尋一樣搜尋MainActivity ,而不是TAG

///////
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMaterialBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMaterialBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        /**
         * fab 是右下角的重完按鈕的id
         */
        binding.fab.setOnClickListener { view ->
            AlertDialog.Builder(this)
                .setTitle("重新玩 (Replay Game) !!!")
                .setMessage("確定嗎  (Are you sure) ?")
                .setPositiveButton(getString(R.string.ok), {dialog,which ->      // 確定, 當點擊確定代表要重玩, 監聽器用lambda表示 , lambda表示用 一對大括號{} , 大括號裡面第一個參數用dialog ,第二個參數叫你按了哪一種按鈕(which) , 接著用lambda(->) ,接重置方法
                    secretNumber.reset()
                    counter.setText(secretNumber.count.toString())               // 重玩方法執行完後,在將0 setText回去
                    ed_number.setText("")                                        // 重玩後把輸入框先前輸入的清掉
                })
                .setNeutralButton(getString(R.string.Cancel),null)        // 取消 , 目前還未對取消做監聽器處理,所以按了會沒反應
                .show()
        }
        counter.setText(secretNumber.count.toString())                           // counter 是已猜次數TextView 的 ID
        Log.d(TAG,"秘密數字(secret) : " + secretNumber.secret)
    }
///////

    fun check(view : View){                                                       // ok按鈕的方法 , 要跟xml > bt_ok按鈕onClick欄位寫一樣名稱,大小寫都要一樣
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

    }


}