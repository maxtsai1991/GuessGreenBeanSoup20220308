package com.max.guess;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class sharedpreferencesActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharedpreferences);
        findViews();
        handleBottomNavigationView();
    }

    private void findViews() {
        // 3.1 取得BottomNavigationView參考
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

    /**
     * 3. 切換頁面程式
     */
    private void handleBottomNavigationView() {
        // 3.2 取得NavController物件
        NavController navController = Navigation.findNavController(this, R.id.navHost);
        // 3.3 加入 導覽功能 至 頁籤導覽元件
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

}