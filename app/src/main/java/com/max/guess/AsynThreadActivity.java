package com.max.guess;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

    /**
     *
     * 該範例邏輯 : 點擊按鈕 > 執行耗時任務,模擬網路請求(字串與1到99整數相加,並且睡眠6秒鐘),透過Handle類及Message類(sendMessage方法&handleMessage方法),將子線程傳遞資料給主線程做處理
     * 參考 : 1.  https://www.bilibili.com/video/BV19U4y1G7mk/?spm_id_from=333.788.recommend_more_video.1
     *       2.  https://www.bilibili.com/video/BV1j54y1H7Kj/?spm_id_from=333.788.recommend_more_video.0
     *
     * 29-Android中的异步任务与多线程,子林Android (bilibili參考 : https://www.bilibili.com/video/BV19U4y1G7mk/?spm_id_from=333.788.recommend_more_video.1) :
     * 同步 VS 異步
     * 同步 : 執行程序時,如果沒有收到執行結果,就一直等,不繼續往下執行,直到收到執行結果,才接著往下執行
     * 異步 : 執行程序時,如果遇到需要等待的任務,就另外開闢一個子線程去執行它,自己繼續往下執行其他程序。子線程有結果時,會將結果發給主線程。
     * 線程 : 一個執行過程。
     * 多線程 : 多個執行過程。
     *
     * 主線程(UI線程) : APP一啟動,本身就是一個線程,負責顯示畫面跟使用者互動。主線程不能執行網路請求/文件讀寫等耗時操作
     * 子線程        : 不能執行UI刷新
     *
     * 30-Android多線程通信 - Handler機制 , 子林Android (bilibili 參考 : https://www.bilibili.com/video/BV1j54y1H7Kj/?spm_id_from=333.788.recommend_more_video.0)
     *      1.   子線程 與 主線程之間傳遞消息的機制
     *      2.   Handler機制主要的幾個腳色 : Handler , Message , Looper , MessageQueue
     *      3.    流程圖 :
     *              子線程 -------SendMessage(拿到主線程的handler,向主線程發消息) ----> 主線程的Handler系統 -------handleMessage(從消息對列中拿到消息後,交給主線程) ----> 主線程(有Handler系統)
     *      4.   主線程需要有一個handleMessage()方法,會有黃色底警告(是因為內存洩漏),解決方法在Handler參數放入Looper.myLooper()
     *      4-1. 當handler收到消息(Message)後,會執行handleMessage{}括號內的程式碼
     *      5.   子線程需要一個消息(Message)物件 EX : Message message = new Message();
     *      5-1. .what方法,通常是用來區分誰發的消息的,因為有可能在子線程裡很多人發消息給主線程 EX : message.what = 0;
     *      5-2. .obj方法,要傳遞字串 EX : message.obj = stringFromNet;
     *      5-3. 當消息建構好,就可以發消息給主執行緒,用主線程的Handler給主線程發消息 EX : mHandler.sendMessage(message);
     *      6.   在handleMessage判斷要取得哪個子線程發的消息 , 當為0標記的線程就執行if裡的程式碼 EX : if(msg.what == 0){ }
     *      6-1  取出0標記的線程的字串 EX : String strData = (String) msg.obj;
     *      6-2  貼上字串 EX : tvContent.setText(strData);
     */
public class AsynThreadActivity extends AppCompatActivity {
    private TextView tvContent;
    private String strFromNet;

        // 4. 主線程需要有一個handleMessage()方法,會有黃色底警告(是因為內存洩漏),解決方法在Handler參數放入Looper.myLooper()
    private Handler mHandler = new Handler(Looper.myLooper()){
        // 4-1. 當handler收到消息(Message)後,會執行handleMessage{}括號內的程式碼
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            // 當handler收到消息(Message)後,會執行{}括號內的程式碼 , 參數msg就是子線程傳遞過來的消息

            // 6. 判斷是哪個子線程發的消息 , 當為0標記的線程就執行if裡的程式碼
            if(msg.what == 0){
                // 6-1 取出0標記的線程的字串
                String strData = (String) msg.obj;
                // 6-2 貼上字串
                tvContent.setText(strData);
                Toast.makeText(AsynThreadActivity.this, "主線程收到子線程的message , 並使用handleMessage處理完成!~", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asyn_thread);

        tvContent = findViewById(R.id.tv_content);
    }

    /**
     * 假設點擊按鈕,做一個耗時任務
     */
    public void start(View view) {
        Toast.makeText(this, "開始請求 !", Toast.LENGTH_SHORT).show();

        /**
         * 子線程 寫法有二
         */
        // 寫法一
        new Thread(new Runnable() {
            @Override
            public void run() {
                String stringFromNet = getStringFromNet();
                //  5.  子線程需要一個消息(Message)物件
                Message message = new Message();
                // 5-1. .what方法,通常是用來區分(標記)誰發的消息的,因為有可能在子線程裡很多人發消息給主線程
                message.what = 0;
                // 5-2. 該範例是要傳遞字串,所以可使用.obj方法
                message.obj = stringFromNet;
                // 5-3. 當消息建構好,就可以發消息給主執行緒,用主線程的Handler給主線程發消息
                mHandler.sendMessage(message);
            }
        }).start();

        // 寫法二
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
            }
        });
        thread.start();

        // 主線程,直接執行畫面處理 , 註解掉是因為要使用 handle類
//        strFromNet = getStringFromNet();
//        tvContent.setText(strFromNet);
//        Toast.makeText(this, "任務完成 !", Toast.LENGTH_SHORT).show();
    }

    private String getStringFromNet() {
        // 假設從網路獲取一個字符串
        String result = "";
        StringBuilder stringBuilder = new StringBuilder();

        //模擬一個耗時操作
        for (int i = 0 ; i < 100 ; i++){
            stringBuilder.append("\"字串加整數\"" + " + " + i +"\n");
        }

        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        result = stringBuilder.toString();
        return result;
    }
}