package com.max.guess

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * 10-6 Fragment的運作原理、生命週期 筆記 :
 * 1.  一個Activity 可以放多個 Fragment
 * 2.  建立Activity(NewsActivity.kt) : com.max.guess > New > Empty Activity > 命名:NewsActivity
 * 3.  建立Fragment(NewsFragment.kt) : com.max.guess > New > Kotlin File/Class > 命名:NewsFragment
 * 4.  NewsFragment.kt : 一定要繼承Fragment(),記得要呼叫空的建構子, 覆寫onCreareView(快捷鍵Ctrl + O), 將return後面改成inflater.inflate(Layout檔,) EX : return inflater.inflate(R.layout.fragment_news,container,false)
 * 5.  建立NewsFragment的Layout檔 : res > layout > (右鍵)New > Layout resource file > 命名: fragment_news.xml , Root element : 改成ConstraintLayout > 拉一個TextView命名: 最新消息 , 在TextView下放一個RecyclerView
 * 6.  activity_news.xml : 畫面分兩半 , 中間上方有個Guidelines按鈕點進去 > 選擇 Add Horizontal Guideline (標準線,拉標準線原因是方便將畫面分兩半,比較好對齊) > 看到標準線後,雙擊會變成百分比顯示 > 在將標準線拉到畫面中間 (50 %) > 之後拉一個ConstraintLayout(Layouts > ConstraintLayout)
 *     將ConstraintLayout左右及上都對齊父元件的ConstraintLayout,下則對齊標準線 > 將ConstraintLayout高寬改成match_parent , 這個ConstraintLayout位置就是未來要擺放Fragment畫面(NewsFragment.kt)的地方 , ConstraintLayout元件一定要設一個ID : container
 * 7.  NewsActivity.kt : 使用fragmentTransaction物件要從supportFragmentManager得到,這時候代表我的交易動作要開始了,在呼叫.beginTransaction()方法 EX : val fragmentTransaction = supportFragmentManager.beginTransaction()
 *     fragmentTransaction在去呼叫add()方法 , add(加入activity_news.xml的ConstraintLayout的ID(container) , 放入Fragment(使用單例模式) ) EX : fragmentTransaction.add(R.id.container,NewsFragment.instance)
 * 8.  最後fragmentTransaction在呼叫.commit()方法,及代表交易開始
 * 9.  NewsFragment.kt : 建一個companion object { } , 希望以後呼叫NewsFragment的方法,就可以產生一個物件 EX : companion object{ val instance : NewsFragment by lazy { NewsFragment() } }
 * 10. RecyclerViewMainActivity.kt : 在 functions list集合 增加 News(最新消息) 字串 EX : "News(最新消息)" , 在跳到functionClicked方法 (快捷鍵Ctrl + F12) , 添加position Intent跳轉程式碼 EX : 4 -> startActivity(Intent(this,NewsActivity::class.java))
 *
 * -------- Fragment生命週期補充說明 --------
 * 將Fragment(NewsFragment.kt)加進去Activity(NewsActivity.kt)之後,
 * Fragment的onAttach會被呼叫 > 接下來是onCreate會被呼叫 > 再來是onCreateView > 之後是onActivityCreated會被呼叫 > 再來自動去執行onStart方法,等到它到畫面上之後 > onResume方法會被呼叫,這個時候Fragment就在畫面上了
 * 如果使用者按下了返回鍵,讓這個Activity(NewsActivity.kt)結束的時候,
 * 裡面Fragment(NewsFragment.kt)的onPause會被呼叫 > onStop也接著被呼叫
 * 在我們結束時候,畫面消失之後,會再執行onDestroyView > 然後再執行onDestroy > 最後則會自動執行onDetach
 * 執行時 : onAttach > onCreate > onCreateView > onActivityCreated > onStart > onResume
 * 結束時 : onPause > onStop > onDestroyView > onDestroy > onDetach
 * -------- Fragment生命週期補充說明 --------
 *
 */

class NewsFragment : Fragment(){
    companion object{
        val instance : NewsFragment by lazy { // 此為單例模式 , Kotline寫法: 此寫法用意是不要一開始就生出來, 我跟你要的時候,有人跟你要instance的時候,在去執行new出來的動作就好,這樣可以避免NewsFragment一開始就出現的問題,裡面就可以直接呼叫NewsFragment的建構子,這樣就完成單例模式(singleton pattern)設計
            NewsFragment()                    // 只要呼叫instance就會New出NewsFragment物件
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_news,container,false) // attachToRoot參數是說這個Layout要不要在一開始出現的時候,就直接去連接到這個container身上,目前是不用(false),我們有需要在上去就好了
    }

}