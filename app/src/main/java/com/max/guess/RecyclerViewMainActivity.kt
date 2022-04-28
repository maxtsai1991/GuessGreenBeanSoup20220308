package com.max.guess

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.max.guess.data.Event
import com.max.guess.data.EventResult
import kotlinx.android.synthetic.main.activity_recycler_view_main.*
import kotlinx.android.synthetic.main.row_function.view.*
import org.json.JSONArray
import java.net.URL

/**
 * 1. 建立RecyclerViewMainActivity原因備忘錄 :
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
 * RecyclerView 筆記 :
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
 *  10.  即完成
 */

/**
 *  9-1 UI執行緒不能執行耗時工作,Android網路連線會遇到的問題 筆記:
 *  1. 例外NetworkOnMainThreadException (Android網路連線的工作不能在MainThread主執行緒,因為主執行緒是要跟使用者互動的),所以要用子執行緒去跑耗時工作 EX : Thread { }.start()
 *  2. 例外SecurityException:Permission denied(missing INTERNET permission?), 解決方法:要去manifests加網路權限 EX : 在manifests 加入 <uses-permission android:name="android.permission.INTERNET"/>
 *  3. 例外SocketException: socket failed: EPERM(operation not permitted) 解決方法: 將模擬器上該app移除 再重新build
 *  4. 例外IOException: Cleartext HTTP traffic to 要連線的網址 not permitted 解決方法: 允許使用非https , 要去manifests 的<application 標籤裡面加上  android:usesCleartextTraffic="true"
 */

/**
 *  9-2 讀取網路上的JSON資料並解析它 筆記:
 *  影片裡api網址 : "http://api.snooker.org/?t=5&s=2021"
 *  這裡範例改用api網址 : "https://cloud.culture.tw/frontsite/trans/SearchShowAction.do?method=doFindTypeJ&category=6" (是JsonArray裡面包著JsonObject)
 *  步驟:
 *      1. 開一個子執行緒 EX : Thread { }.start()
 *      2. 貼上API URL EX : val data = URL("https://cloud.culture.tw/frontsite/trans/SearchShowAction.do?method=doFindTypeJ&category=6").readText()
 *      3. 測試有無抓到資料 , 並在Logcat上查看 EX : println(data)
 *      4. 可以看該API 網站 裡面有 JSON陣列 包著很多 JSON物件 , 物件裡面又有屬性 EX : UID 、 title
 *      5. 因為此API 網站 第一個看到的是 [] 中括號 ,所以餵給它的時候,它是陣列,請使用JSONArray,它可以接收字串資料,然後產生JSON的陣列,最後把JSON的陣列存起來 EX : val array = JSONArray(data)
 *      6. 接著用for迴圈一個一個印出來 ,這樣就可以把所有的JSONArray裡面的內容通通印出來 EX :  for (i in 0..array.length()-1){ }
 *      7. 回到JSON格式的時候 , 裡面是個JSON物件 ( 用花括號{}包起來 ) , 所以要取得JSON物件 ,for迴圈裏面 EX : val obj = array.getJSONObject(i)
 *      8. 印出想要的屬性所對應的資料 ,這裡想要UID 、 title屬性資料 EX : println("UID : " + obj.getString("UID")+ " , title : " +obj.getString("title"))
 *      9. 第8點要注意 如果屬性資料沒有雙引號 則使用 getint("屬性名稱") , 反之有雙引號則使用getString("屬性名稱")
 */

/**
 * 9-3 使用外掛工具產生data class,為什麼? A :因Json資料,有時候真的蠻複雜的,一個一個比對,去建立它的類別或data class是很辛苦的,所以才使用外掛幫助
 *  data class(資料類別) 補充說明 : 專門讓我存放資料模別
 *  使用FireFox瀏覽器(因可直接查看Json API格式),貼上範例網址(https://cloud.culture.tw/frontsite/trans/SearchShowAction.do?method=doFindTypeJ&category=6)
 *  1.Android Studio添加外掛,雙擊兩下Shift,搜尋:Plugins,在Marketplace搜尋:JSON To Kotlin Class(JsonToKotlinClass),並安裝它,安裝完要重開Android Studio
 *  2.使用FireFox瀏覽器去到範例網址,切到原始資料標籤,複製範例的json格式資料
 *  3.新建 Kotlin Class (名稱叫Model(資料模型)), 插入外掛(快捷鍵Alt + Insert), 選擇 Kotlin data classes from JSON, 先去Advanced設定>選擇Other標籤,將"Enable Order By Alphabetical"(按照英文字母排序)勾選取消, 再貼上範例的json格式資料並取名(EX:Event), 點選Generate
 *  4.即創建完data class(範例Json格式的資料類別)
 */

/**
 * 9-4 使用第三方類別庫Gson快速解析JSON得到集合 筆記:
 * Gson GitHub : https://github.com/google/gson
 * 添加Gson Libs 在 build.gradle(Module) EX : implementation 'com.google.code.gson:gson:2.9.0'
 * 1. 呼叫Gson類別庫的建構子,先把這個物件建立出來 EX : Gson()
 * 2. 使用fromJson()方法 EX : Gson().fromJson( )
 * 3. 放入Json資料, Json資料就是從網路上讀到的Json資料, 因為先前整個資料,所產生的是一個EventResult ( class EventResult : ArrayList<Event>() ) ,
 *    再加上類別標示它,告訴它未來會得到一個EventResult,EventResult裡面就是一個集合( EventResult : ArrayList<Event>() ),那可以從集合裡面取出很多的Event EX : Gson().fromJson(data, EventResult::class.java)
 * 4. 印出全部轉成Gson資料,用forEach EX : result.forEach { Log.d(TAG, "RecyclerViewActivity_onCreate: $it");  }
 *
 */

/**
 *  10-1 下拉選單 - Spinner顯示固定個數的資料 筆記:
 *  補充說明:
 *  清單元件(下拉選單)使用Spinner類別 , Spinner類別可以個別設定資料及Layout長相
 *  清單元件上面都會有一個方法叫setAdapter,而因為用Kotlin語言,所以直接用adapter方法
 *  1.  activity_recycler_view_main.xml 添加 Spinner元件(元件位置:Containers > Spinner), 設置Spinner ID
 *  2.  定義Spinner資料 EX : val colors = arrayOf("Red" , "Green" , "Blue")
 *  3.  設置Spinner的Layout畫面及資料 ArrayAdapter<T>(Context: Context, Layout , 資料) EX : val adapter = ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,colors)
 *  3-1.可修改Spinner下拉選單的樣式 EX : adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
 *  3-2.Layout不一定要自己產生,可以用Android內建的,可參考C:\Users\s8604\AppData\Local\Android\Sdk\platforms\android-31\data\res\layout底下,有很多內建的Layout,該章節使用simple_spinner_item當範例
 *  4.  將SpinnerLayout及資料設置給Spinner的adapter EX : spinner.adapter = adapter
 *  5.  設置Spinner點擊事件,使用關鍵字object : OnItemSelectedListener ,需要覆寫兩個方法(快捷鍵:Alt+Enter),兩方法:1.沒有選任何東西 2.已經選了某樣東西
 *  6.  設計點選下拉選單選項方法 , 該章節示範選了某樣東西的方法  EX : override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {  Log.d(TAG, "onItemSelected: ${colors[position]}"); }
 */

/**
 *  10-2 Android 6 之後一定要的危險權限機制 (該章節使用相機權限) 筆記:
 *  在manifests添加
 *  <uses-feature android:name="android.hardware.camera" android:required="true"/>          // 有相機的硬體才可安裝這個APP
 *  <uses-permission android:name="android.permission.CAMERA"/>                             // 添加相機危險權限
 *  1.  在RecyclerView 清單 , 添加相機選項 EX : val functions = listOf<String>( "Camer(開啟相機)" )
 *  2.  在清單點擊事件,添加實作程式碼
 *  2-1.先設計跳轉相機方法,這個跳轉相機方法不能隨意執行,因為這個工作一定要經過使用者同意過危險權限(開啟相機)之後,才可執行這段程式碼,所以先將方法獨立出去(快捷鍵Ctrl + Alt + M) 命名:takePhoto() ,這個獨立出來的方法在符合條件的時候才會執行它
 *  2-2.撰寫if判斷式 ,判斷有無取得permission , 程式邏輯: 當取得Permission就開啟相機, 反之還沒有即跳出對話框 EX :  if(permission == PackageManager.PERMISSION_GRANTED){ }else{ }
 *  3.  覆寫危險彈窗選項點選後方法(onRequestPermissionsResult),細項說明及程式邏輯參考下面逐行註解補充
 */

/**
 * 10-3 Toolbar上方的選單(Menu)設計 筆記:
 * 補充說明:APP的上方叫Toolbar / ActionBar , 這個位置可以設計有功能表 , 設計menu功能表如下
 * 1.  res 右鍵 > New > 新增資源的資料夾(Android Resource Directory) > Resource type欄位 選擇menu > ok
 * 2.  在資源的資料夾 右鍵 > New > Menu resource file > File name欄位 打上自定義名稱 (EX : menu_main.xml)
 * 3.  拉Menu Item(Palette)到下面menu(Component Tree)底下
 * 4.  訂定title & id & icon & showAsAction (EX : title為Cache , id為action_cache , icon為stat_sys_download下載圖示 , showAsAction(圖示要顯示在裡面或是外面的欄位))
 * 4-1.showAsAction : always(一律顯示) 、 never(不顯示圖示) 、 ifRoom(若有空間則顯示) ,此範例是選擇ifRoom
 * 回到RecyclerViewMainActivity.kt
 * 1.  覆寫兩個方法(快捷鍵Ctrl + o) : onCreateOptionsMenu (當我們Activity要出現的時候,會去要一個Menu物件,就是上面表單物件) & onOptionsltemSelected (當使用者去點選它之後)
 * 2.  onCreateOptionsMenu覆寫方法說明(此方法是完成建立跟顯示) :
 * 2-1.使用menuInflater類別下.inflate方法(給予設計圖(上面新增的xml) , 想要inflate的物件,剛好方法,傳進來的時候就有menu物件) EX : menuInflater.inflate(R.menu.menu_main, menu)
 * 3. onOptionsltemSelected覆寫方法說明(此方法是當使用者,如果點選其中一個選項時候,它一定會跑到這個方法來) :
 * 3-1.判斷使用者點選哪一個選項,查看id,這個id值假如是等於R.id.action_cache (menu的id) 代表它按了下載圖選項,在做點擊事件的程式設計 EX : if(item.itemId == R.id.action_cache){ Log.d(TAG, "Cache selected"); }
 */

class RecyclerViewMainActivity : AppCompatActivity() {
    private val REQUEST_CODE_CAMERA = 100
    val TAG = RecyclerViewMainActivity::class.java.simpleName
    var cacheService : Intent? = null

    /** 7-2章節 新增Guess game(猜數字遊戲) & Record list(紀錄清單) 分別導到MaterialActivity & RecordListActivity*/
    val functions = listOf<String>(     //array字串,listof是一個集合,裏頭放字串
        "0.Camer(開啟相機)",
        "1.Guess game(猜數字遊戲)",
        "2.Record list(遊戲紀錄清單)",
        "3.Download coupons(下載優惠券)",
        "4.News(最新消息)",
        "5.Snooker(網路API)",
        "6.Maps(地圖)",
        "7.LifeCycleDemo",
        "8.ViewModel",
        "9.ViewModelsUseFragment",
        "10.LiveData",
        "11.RecyclerViewDataTest5",
        "12.RecyclerViewDataTest6",
        "13.RecyclerViewDataTest7",
        "14.RecyclerViewDataTest8",
        "15.RecyclerViewDataTest9",
        "16.RecyclerViewDataTest10",
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view_main)

        Thread {
            val data = URL("https://cloud.culture.tw/frontsite/trans/SearchShowAction.do?method=doFindTypeJ&category=6").readText() // 將全部資料讀進來,而此方法無法作後續篩選處理
//            val data = URL("http://api.snooker.org/?t=5&s=2021").readText() // 將全部資料讀進來,而此方法無法作後續篩選處理
//            println(data)

            /**
             * 9-2 Json解析資料(內建類別庫解析資料) 寫法
             * 備註: 下面解析是使用此API : val data = URL("https://cloud.culture.tw/frontsite/trans/SearchShowAction.do?method=doFindTypeJ&category=6").readText()
             */
//            val array = JSONArray(data)
//            for (i in 0..array.length()-1){
//                val obj = array.getJSONObject(i) // getJSONObject(index : Int)說明 : 當我給它位置得值,它就給我JSONObject資料
//                val id = obj.getString("UID")
//                val title = obj.getString("title")
//                val startDate = obj.getString("startDate")
//                val endDate = obj.getString("endDate")
//                println("UID : " + id + " , title : " + title + " , startDate : " + startDate + " , endDate : " + endDate) // 取得API上 UID、title、startDate、endDate屬性資料
//            }

            /**
             * 9-4 Gson解析資料(快速解析的類別庫)抓到全部資料 寫法
             */
            val result = Gson().fromJson(data, EventResult::class.java)
            result.forEach {
                Log.d(TAG, "RecyclerViewActivity_onCreate: $it");
            }

        }.start()

        // RecyclerView (清單功能表)
        recycler.layoutManager = LinearLayoutManager(this) // 從layout裡面取得RecyclerView(recycler是layout RecyclerView的ID),告訴RecyclerView 元件設定要設定什麼
        recycler.setHasFixedSize(true)                            // 設定畫面大小是不是固定式
        recycler.adapter = FunctionAdapter()                      // 設定設計adapter , 1. 可以設計adapter為內部類別 (此頁面的adapter為內部類別,inner class) 2. 也可以獨立出adapter class檔

        // 10-1 spinner
        val colors = arrayOf("Red" , "Green" , "Blue") // 假資料
        val adapter = ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,colors) // 如資料來源是Array(EX : val colors = arrayOf("Red" , "Green" , "Blue") ),即使用ArrayAdapter,ArrayAdapter裏頭它可以直接給一個型態,這裡用字串String
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)                 // 修改下拉選單樣式
        spinner.adapter = adapter                                                                    // 將下拉選單樣式及資料設定給adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{                // Spinner點擊事件
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.d(TAG, "onItemSelected: ${colors[position]}");
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }
    // Adapter (注意事項: 繼承Adapter要留意要選擇androidx.recyclerview.widget.recyclerView)
    inner class FunctionAdapter() : RecyclerView.Adapter<FunctionHolder>(){ // 需要繼承,而且要呼叫父類別的建構子 EX: 這裡建構子為空建構子

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FunctionHolder { // 此方法一定要回傳一個holder
            val view = LayoutInflater.from(parent.context)                // 產生一個view,LayoutInflater類別使用.from()方法,此方法需要一個context,可以從parent身上去取得context
                .inflate(R.layout.row_function, parent, false)// LayoutInflater工具身上有一個inflate方法,他可以吃一個Resource Layout資料 EX : .inflate(R.layout.row_function, parent, false)

            val holder = FunctionHolder(view)
            return holder
        }

        override fun onBindViewHolder(holder: FunctionHolder, position: Int) { // onBindViewHolder說明: 當資料有的時候,會被自動呼叫
            holder.nameText.text = functions.get(position)                     // position就是當他要顯示第幾列資料的時候會傳進來的一個整數值
            //  holder.itemView (holder裡面的物件,叫做整塊的view)這整塊的view如果被人按了,就會觸發點擊事件
            holder.itemView.setOnClickListener {
                functionClicked(position)                                     // 將點擊選項方法獨立出來
                Log.d(TAG, "recycleritem : " + position)                 // 測試點擊選項的position
            }

        }

        override fun getItemCount(): Int { // 裡面有幾筆資料 , 這邊範例資料為假資料(在該頁面的屬性) EX : val functions
            return functions.size
        }

    }

    private fun functionClicked(position: Int) {
        /**
         * position 說明:
         * position 0 Camer(打開相機)選項
         * position 1 Guess game(猜數字遊戲)選項
         * position 2 Record list(遊戲紀錄清單)選項
         * position 4 News(最新消息)選項
         * position 5 Snooker(網路API資料)
         * position 7 Android Jetpack 第二章 lifecycledemo範例 : LifecycleOwner（被观察者）和LifecycleObserver（观察者）。即通过观察者模式，实现对页面生命周期的监听 如何使用 ? A : 查看Log (startGetLocation() & stopGetLocation())
         * position 8 Android Jetpack 入門課程(騰訊課程) ViewModel的使用 參考 : https://ke.qq.com/course/4128266 , 使用ViewModel 當翻轉螢幕時,不會銷毀畫面Data ; 點擊按鈕及加一
         * position 9 Android Jetpack 入門課程(騰訊課程) ViewModel實戰案例 參考 : https://ke.qq.com/course/4128266/12397062326779402 , 當點擊按鈕加一,而Fragment A & B 都會一起改變,因為FragmentA & FragmentB 共享 TextView(tvCount)使用 ViewModel & MutableLiveData & observe(觀察者)
         * 以此類推
         * 7-2章節 目前只有導兩個不同頁面 ,其他以外未設定, 就用else -> return
         */
        when(position){// 判斷position是什麼樣的值
            0 -> {
                val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) // .checkSelfPermission(Context , 傳入打算檢查的權限字串,這個權限已經訂在Maifest類別(android) EX :  Manifest.permission.CAMERA) ,這個方法呼叫會得到INT整數值
                if(permission == PackageManager.PERMISSION_GRANTED){                                         // 判斷有無取得permission , 已取得為0沒有取得為-1 ,程式設計的時候不要使用0 or -1 這種常數,這個常數值已經定義在PackageManager類別,程式邏輯: 當取得Permission就開啟相機, 反之還沒有即跳出對話框
                    takePhoto() // 開啟相機獨立出的方法
                }else{
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CODE_CAMERA) // 跳出對話框 ,ActivityCompat類別下的.requestPermissions(context,字串陣列(放入所要的權限字串),請求代碼)方法,
                // 請求代碼補充說明:未來跳出對話框之後,使用者允許或是拒絕,都會回到我們的Activity,需要有個編號值,好知道它是從這邊出去的請求值,不要用常數值,而是要自定義名稱(EX : REQUEST_CODE_CAMERA),將REQUEST_CODE_CAMERA定義在最上方的屬性欄位(快捷鍵:燈泡熱鍵 > Create property 'REQUEST_CODE_CAMERA'),這個值就可以用來判別返回Activity時候的判定值
                // 當使用者按下對話框某個選項,會回到該Activity裡面的一個方法,需覆寫該方法,方法叫onRequestPermissionsResult(){}方法 (該方法使用快捷鍵Ctrl + O > 輸入叫做onRequestPermissionsResult)
                }
            }
            1 -> startActivity(Intent(this,MaterialActivity::class.java))
            2 -> startActivity(Intent(this,RecordListActivity::class.java))
            4 -> startActivity(Intent(this,NewsActivity::class.java))
            5 -> startActivity(Intent(this,SnookerActivity::class.java))
            7 -> startActivity(Intent(this,LifeCycleDemoActivity::class.java))
            8 -> startActivity(Intent(this,ViewModelActivity::class.java))
            9 -> startActivity(Intent(this,ViewModelsUseFragmentActivity::class.java))
            10 -> startActivity(Intent(this,LiveDataActivity::class.java))
            else -> return
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) { // 危險彈窗選項點選後所需覆寫的方法(CtrL + O ) , onRequestPermissionsResult建構子說明()傳入三個參數 : 1.請求代碼 2.從requestPermissions()方法傳過來的字串陣列(EX : arrayOf(Manifest.permission.CAMERA)) 3.使用者所按下的結果
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_CODE_CAMERA){ // if判斷說明,如果使用者按下的是requestCode,剛才去的是,是不是同樣詢問的工作,如果是的話,再多進行一層判斷,
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){//  if判斷說明,如果我的grantResults它是一個多個陣列,可以使用第0個就可以了,拿來比較,看使用者是不是PERMISSION_GRANTED,如果是grantResults就執行打開相機
                takePhoto()
            }
        }
    }

    private fun takePhoto() {      // 開啟相機獨立出的方法
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE) // 設計Intent物件,利用身上的特別字串 EX : MediaStore.ACTION_IMAGE_CAPTURE ,就可以啟用我們的相機
        startActivity(intent)
    }

    class FunctionHolder(view: View) : RecyclerView.ViewHolder(view){ // 注意事項 Holder 有建構子EX:(view: View) ,直接繼承RecyclerView裡面的ViewHolder,他只有一個建構子就是view物件
        var nameText: TextView = view.name                            // 這裡的程式碼,取決於自己建立一列資料的長相EX : row_function.xml 裡面的呈現一列資料所有元件
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_cache){
            Log.d(TAG, "Cache selected");
            cacheService = Intent(this,CacheService::class.java)
            startService(cacheService)
        }
        return super.onOptionsItemSelected(item)
    }
    /**
     * 先註解onStop()方法,此方法是10-4 Service 的 , 因為不註解,點選Snooker選項會報錯,已反應給老師 , 2022/04/11 已解決 , 增加cacheService是否為空值判斷式
     */
    override fun onStop() {
        super.onStop()
        if(cacheService != null){
            stopService(cacheService)
        }
    }
}