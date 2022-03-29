package com.max.guess

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_recycler_view_main.*
import kotlinx.android.synthetic.main.row_function.view.*

/**
 * 1. 建立RecyclerViewMainActivity原因備忘錄:
 *      此RecyclerViewMainActivity為綠豆湯影片7-1章節建立,因影片是直接砍掉原有MainActivity程式碼當示範,
 *      而為了保留原有寫的程式碼,因而另開此RecyclerViewMainActivity,往後影片示範動到MainActivity就動該頁(RecyclerViewMainActivity)
 *
 * 2. 切換模擬器初始畫面(首頁)備忘錄:
 *             <intent-filter>
                    <action android:name="android.intent.action.MAIN" />

                    <category android:name="android.intent.category.LAUNCHER" />
                </intent-filter>
 *     在manifests 的<activity 及 </activity> 標籤裡面 加上上面的 即可
 * */

/**
 * 一、RecyclerView(回收機制的元件) 筆記:
 *  1.  RecyclerView 需要 adapter來顯示資料
 *  2.  adapter 負責他的資料來源 跟 他的展示樣貌
 *  3.  另外還需要寫一個類別叫ViewHolder類別
 *
 * 二、RecyclerView 觀念備忘錄:
 *  1.  RecyclerView身上必須要設定layoutManager EX: recycler.layoutManager = LinearLayoutManager(this)
 *  1-1.RecyclerView身上要設定畫面大小是不是固定式 EX : recycler.setHasFixedSize(true)
 *  2.  設定adapter(產生一個adapter給RecyclerView) EX : recycler.adapter = FunctionAdapter()
 *  2-1.這adapter必須要繼承RecyclerView.Adapter,並且身上要配置畫面上的Holder儲存器 EX : inner class FunctionAdapter() : RecyclerView.Adapter<FunctionHolder>(){ }
 *  3. viewHolder暫存器類別設計 EX :  class FunctionHolder(view: View) : RecyclerView.ViewHolder(view){ var nameText: TextView = view.name }
 *  4. 再來如果他有客製化的設計,你還必須要設計單列資料的layout的長相和相關的ID要設定好
 *
 *  SOP !!! RecyclerView建置步驟(使用匿名內部類別的adapter和資料來源為假資料(放在屬性)) :
 *  1.   設定RecyclerView的layout管理及細節 EX: recycler.layoutManager = LinearLayoutManager(this) & recycler.setHasFixedSize(true)
 *  2.   建立匿名內部adapter先打好外框 EX: inner class FunctionAdapter() : RecyclerView.Adapter<>(){ }
 *  3.   建立ViewHolder EX : class FunctionHolder(view: View) : RecyclerView.ViewHolder(view){ 裡面打綁定元件的程式碼(ex:var nameText: TextView = view.name) }
 *  4.   創建RecyclerView裡面的單一欄位layout檔 EX : row_function.xml
 *  5.   補上ViewHolder layout (row_function.xml) 元件
 *  6.   回到adapter 補上泛型裡面的viewHolder類別 EX : RecyclerView.Adapter<FunctionHolder> , 並且 補上空的建構子 EX : 括號()
 *  7.   使用燈泡熱鍵 選擇"implement members"選項,自動產生覆寫出三個方法onCreateViewHolder & onBindViewHolder & getItemCount
 *       EX : override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FunctionHolder { } & override fun onBindViewHolder(holder: FunctionHolder, position: Int) { } &  override fun getItemCount(): Int { }
 *  8.   補上覆寫三個方法的實作的程式碼
 *  9.   設計指定adapter,打上自定義方法名字的adapter EX : recycler.adapter = FunctionAdapter()
 *  10.   即完成
 */
class RecyclerViewMainActivity : AppCompatActivity() {
    val TAG = RecyclerViewMainActivity::class.java.simpleName
    val functions = listOf<String>(     //array字串,listof是一個集合,裏頭放字串
        "Camer(打開相機)",
        "Invite friend(邀請朋友)",
        "Parking(停車處)",
        "Download coupons(下載優惠券)",
        "News(最新消息)",
        "Maps(地圖)",
        "1.RecyclerViewDataTest1",
        "2.RecyclerViewDataTest2",
        "3.RecyclerViewDataTest3",
        "4.RecyclerViewDataTest4",
        "5.RecyclerViewDataTest5",
        "6.RecyclerViewDataTest6",
        "7.RecyclerViewDataTest7",
        "8.RecyclerViewDataTest8",
        "9.RecyclerViewDataTest9",
        "10.RecyclerViewDataTest10",

    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view_main)

        // RecyclerView (清單功能表)
        recycler.layoutManager = LinearLayoutManager(this) // 從layout裡面取得RecyclerView(recycler是layout RecyclerView的ID),告訴RecyclerView 元件設定要設定什麼
        recycler.setHasFixedSize(true)                            // 設定畫面大小是不是固定式
        recycler.adapter = FunctionAdapter()                      // 設定設計adapter , 1. 可以設計adapter為內部類別 (此頁面的adapter為內部類別,inner class) 2. 也可以獨立出adapter class檔
    }
    // Adapter (注意事項: 繼承Adapter要留意要選擇androidx.recyclerview.widget.recyclerView)
    inner class FunctionAdapter() : RecyclerView.Adapter<FunctionHolder>(){ // 需要繼承,而且要呼叫父類別的建構子 EX: 這裡建構子為空建構子

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FunctionHolder { // 此方法一定要回傳一個holder
            val view = LayoutInflater.from(parent.context)                // 產生一個view,LayoutInflater類別使用.from()方法,此方法需要一個context,可以從parent身上去取得context
                .inflate(R.layout.row_function, parent, false)// LayoutInflater工具身上有一個inflate方法,他可以吃一個Resource Layout資料 EX : .inflate(R.layout.row_function, parent, false)

            val holder = FunctionHolder(view)
            return holder
        }

        override fun onBindViewHolder(holder: FunctionHolder, position: Int) {
            holder.nameText.text = functions.get(position)              // position就是當他要顯示第幾列資料的時候會傳進來的一個整數值

            // 測試點擊選項監聽事件
//            holder.nameText.setOnClickListener {
//                Log.d(TAG, "recycleritem : " + position)
//            }
        }

        override fun getItemCount(): Int { // 裡面有幾筆資料 , 這邊範例資料為假資料(在該頁面的屬性) EX : val functions
            return functions.size
        }

    }

    class FunctionHolder(view: View) : RecyclerView.ViewHolder(view){ // 注意事項 Holder 有建構子EX:(view: View) ,直接繼承RecyclerView裡面的ViewHolder,他只有一個建構子就是view物件
        var nameText: TextView = view.name                            // 這裡的程式碼,取決於自己建立一列資料的長相EX : row_function.xml 裡面的呈現一列資料所有元件
    }

}