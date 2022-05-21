package com.max.guess;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class ExternalStorageActivity extends AppCompatActivity {
    private static final String TAG = "TAG_MainActivity";
    private final String FILE_NAME = "note.txt";
    private File dir;
    private EditText etNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external_storage);

        // 取得外部儲存體路徑
        dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        findViews();
    }

    private void findViews() {
        etNote = findViewById(R.id.etNote);
    }

    /**
     * 開啟App時，從外部儲存體私有檔案[讀檔]至EditText顯示
     */
    @Override
    protected void onStart() {
        super.onStart();
        if (!checkExternalStorageState()) {
            return;
        }

        // 讀檔
        try (
                FileReader fr = new FileReader(new File(dir, FILE_NAME));
                BufferedReader br = new BufferedReader(fr)
        ) {
            StringBuilder note = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                note.append(line)
                        .append("\n");
            }
            etNote.setText(note);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    /**
     * 離開App時，將筆記內容[存檔]至外部儲存體私有檔案
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (!checkExternalStorageState()) {
            return;
        }

        // 存檔
        final String note = String.valueOf(etNote.getText());
        try (FileWriter fw = new FileWriter(new File(dir, FILE_NAME))) {
            fw.write(note);
            fw.flush();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    /**
     * 檢查外部儲存體狀態
     */
    private boolean checkExternalStorageState() {
        // 取得外部儲存體狀態
        String state = Environment.getExternalStorageState();
        // 確認狀態(是否可讀寫)
        return Environment.MEDIA_MOUNTED.equals(state);
    }

}