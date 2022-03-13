package com.max.guess

import androidx.lifecycle.ViewModel

class GuessViewModel : ViewModel(){
    /**
     * 注意事項:
     * 1. 建立完ViewModel 一定要注意要繼承ViewModel類別(選androidx.lifecycle)
     * 2. 繼承ViewModel類別 必須要呼叫父類別的建構子 ,所以要用括號 ( )
     * 3. 設計需求 EX : 需要counter(猜的次數) , 需要ckeck方法(比較數字的結果)
     * 4. 設計完後, ViewModel 傳遞給前面的Activty ,去通知前面的畫面,我的資料需要改變
     */
}