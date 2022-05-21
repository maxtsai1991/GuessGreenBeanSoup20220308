package com.max.guess.fragment;


import static com.max.guess.util.Constants.KEY_THEME_MODE;
import static com.max.guess.util.Constants.PREFERENCES_FILE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;


import com.max.guess.R;

public class SettingsFragment extends Fragment {
    private AppCompatActivity activity;
    private SharedPreferences sharedPreferences;
    private RadioGroup rgTheme;
    private RadioButton rbLight, rbDark;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = (AppCompatActivity) getActivity();
        // 取得SharedPreferences物件
        sharedPreferences = activity.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        handleActionBar();
        handleRadioButtons();
        handleRadioGroup();
    }

    private void findViews(View view) {
        rgTheme = view.findViewById(R.id.rgTheme);
        rbLight = view.findViewById(R.id.rbLight);
        rbDark = view.findViewById(R.id.rbDark);
    }

    /**
     * ActionBar相關處理
     */
    private void handleActionBar() {
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.textSettings);
        }
    }

    /**
     * RadioButton相關處理
     */
    private void handleRadioButtons() {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            rbDark.setChecked(true);
        } else {
            rbLight.setChecked(true);
        }
    }

    /**
     * RadioGroup相關處理
     */
    private void handleRadioGroup() {
        rgTheme.setOnCheckedChangeListener((group, checkId) -> {
            if (checkId == R.id.rbLight) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                savePreferences(AppCompatDelegate.MODE_NIGHT_NO);
            } else if (checkId == R.id.rbDark) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                savePreferences(AppCompatDelegate.MODE_NIGHT_YES);
            }
        });
    }

    /**
     * 偏好設定檔-存檔
     */
    private void savePreferences(int value) {
        sharedPreferences
            // 開始編輯
            .edit()
            // 寫出資料
            .putInt(KEY_THEME_MODE, value)
            // 存檔
            .apply();
    }
}