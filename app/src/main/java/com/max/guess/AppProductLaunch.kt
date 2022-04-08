package com.max.guess

class AppProductLaunch {
    /**
     * 11-1 Play 商店上架前準備 & 11-2 Google Play 商店APP上架實例 筆記 :
     *
        1. 確認Application ID 是否已被使用 (從build.gradle : Module)
           去https://play.google.com/ > 任意點擊商店APP > 將網址id = 後面改成 自己APP的applicationId EX : "com.max.guess" ,如沒沒有出現app代表可用
        2. 確認應用程式執行正不正常(模擬器上的APP解除安裝,從build一次,以及對自己APP的功能都點選看看)
        3. APP執行畫面截圖兩張(512*512 & 1024*500)
           截圖功能在Logcat的左邊有個相機圖案可使用,可以存到電腦任何處
        4. 準備APP圖示檔及替換,icon圖檔放在res > drawable資料夾下(解析度大概128*128 , manifests icon標籤EX : android:icon="@mipmap/ic_launcher" & android:roundIcon="@mipmap/ic_launcher_round" 都要換)
        5. 建立/產生發行開發金鑰 , 原開發金鑰預設電腦路徑:C:\Users\s8604\.android的debug.keystore
           Android Studio > (上方的工具列)Build > Generate Signed Bundle / APK > 選擇APK >
           選擇Create new > 修改金鑰放置路徑(Key store path) > 訂定密碼Password > 修改別名(Alias)EX : guess > 在訂定別名下密碼Password >
           填寫Certificate資訊 > 點選OK > 勾選記住密碼(Remember passwords) > 選Release >app-release.apk
        6. chrome > 搜尋play console >進去後點擊Launch play console須註冊成為開發者
        7. 付款25美金
        8. 建立應用程式 > 建立標題 > 簡短說明填寫 > 完整說明填寫
        9. 上傳準備的照片
        10. 填寫應用程式的分類 & 信箱
        11. 隱私權政策填寫 (網路上找)
        12. 左)應用程式版本>正式版>建立新版本>上傳app-release.apk>填寫版本名稱
        13. 把左邊需填寫的一一填寫 填寫完避諱有綠色打勾
        14. 左)定價與發布 >填寫免費與付費 >填寫上架的國家
        15. 審核時間不訂 有時候1-2小時 有時1-2天
     */
}