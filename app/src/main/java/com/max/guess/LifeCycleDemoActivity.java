package com.max.guess;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class LifeCycleDemoActivity extends AppCompatActivity {

    private MyLocationListener myLocationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_cycle_demo);

        myLocationListener = new MyLocationListener(this, new MyLocationListener.OnLocationChangedListener() {
            @Override
            public void onChanged(double latitude, double longitude) {
                // 展示收到的位置信息
            }
        });

        /**
         * 将观察者与被观察者绑定
         */
        getLifecycle().addObserver(myLocationListener);
    }
}