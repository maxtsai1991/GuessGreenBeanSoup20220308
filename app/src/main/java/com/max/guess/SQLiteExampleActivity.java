package com.max.guess;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * 碼農日常-『Android studio』SQLite資料庫建立、資料表建立與操作以及Stetho工具 參考 :
 *          https://thumbb13555.pixnet.net/blog/post/317439792-android_studio_sqlite_stetho
 *          https://github.com/thumbb13555/SQLiteExample (Github)
 *          https://mrraybox.blogspot.com/2017/01/android-sqlite-onupgrade.html (SQLite onUpgrade方法的運用)
 *          https://www.itread01.com/study/sqlite-distinct-keyword.html (SQLite 教學)
 */
public class SQLiteExampleActivity extends AppCompatActivity {
    String TAG = SQLiteExampleActivity.class.getSimpleName() + "My";

    private final String DB_NAME = "MyList.db";
    private String TABLE_NAME = "MyTable";
    private final int DB_VERSION = 1;
    SQLiteDataBaseHelper mDBHelper;

    // 取得所有資料
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();

    // 取得被選中的項目資料
    ArrayList<HashMap<String, String>> getNowArray = new ArrayList<>();

    EditText edName, edPhone, edHobby, edElse;
    Button btCreate, btModify, btClear;
    MyAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_example);

        // 使用Stetho工具 , 加入 Stetho 初始化
        Stetho.initializeWithDefaults(this);

        /**
         * 初始化資料庫
         */
        mDBHelper = new SQLiteDataBaseHelper(this, DB_NAME
                , null, DB_VERSION, TABLE_NAME);

        // 確認是否存在資料表，沒有則新增
        mDBHelper.chickTable();

        // 取得所有的資料表(Table名稱)
        mDBHelper.getTables();

        // 撈取資料表內所有資料
        arrayList = mDBHelper.showAll();

        // 連接所有元件
        itemSetting();

        // 設置RecyclerView
        recyclerViewSetting();

        // 設置按鈕功能
        buttonFunction();

    }// onCreate End

    /**
     * 設置按鈕功能
     */
    private void buttonFunction() {
        // 清空
        btClear.setOnClickListener(v -> {
            clearAll();//清空目前所選以及所有editText
        });
        // 新增
        btCreate.setOnClickListener(v -> {
            mDBHelper.addData(edName.getText().toString()
                    ,edPhone.getText().toString()
                    ,edHobby.getText().toString()
                    ,edElse.getText().toString());
            arrayList = mDBHelper.showAll();
            myAdapter.notifyDataSetChanged();
            clearAll();
        });
        // 修改
        btModify.setOnClickListener(v -> {
            mDBHelper.modify(getNowArray.get(0).get("id")
                    ,edName.getText().toString()
                    ,edPhone.getText().toString()
                    ,edHobby.getText().toString()
                    ,edElse.getText().toString());
            arrayList = mDBHelper.showAll();
            myAdapter.notifyDataSetChanged();
            clearAll();//清空目前所選以及所有editText

        });
    }

    /**
     * 清空目前所選以及所有editText
     */
    private void clearAll() {
        edName.setText("");
        edElse.setText("");
        edHobby.setText("");
        edPhone.setText("");
        getNowArray.clear();
    }

    /**
     * 設置RecyclerView
     */
    private void recyclerViewSetting() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new MyAdapter();
        // 設置Adapter
        recyclerView.setAdapter(myAdapter);
        // 設置RecyclerView手勢功能
        setRecyclerFunction(recyclerView);
    }

    /**
     * 連接所有元件
     */
    private void itemSetting() {
        btCreate = findViewById(R.id.button_Create);
        btModify = findViewById(R.id.button_Modify);
        btClear = findViewById(R.id.button_Clear);
        edName = findViewById(R.id.editText_Name);
        edPhone = findViewById(R.id.editText_Phone);
        edHobby = findViewById(R.id.editText_Hobby);
        edElse = findViewById(R.id.editText_else);
    }

    /**
     * 設置Adapter
     */
    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.tvTitle.setText(arrayList.get(position).get("name"));

            holder.itemView.setOnClickListener((v) -> {
                getNowArray.clear();
                getNowArray = mDBHelper.searchById(arrayList.get(position).get("id"));
                try {
                    edName.setText(getNowArray.get(0).get("name"));
                    edPhone.setText(getNowArray.get(0).get("phone"));
                    edHobby.setText(getNowArray.get(0).get("hobby"));
                    edElse.setText(getNowArray.get(0).get("elseInfo"));
                } catch (Exception e) {
                    Log.d(TAG, "onBindViewHolder: " + e.getMessage());
                }

            });

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(android.R.id.text1);
            }
        }
    }

    private void setRecyclerFunction(RecyclerView recyclerView){

        /**
         * 設置RecyclerView手勢功能
         */
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {//設置RecyclerView手勢功能
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                switch (direction){
                    case ItemTouchHelper.LEFT:
                    case ItemTouchHelper.RIGHT:
                        mDBHelper.deleteByIdEZ(arrayList.get(position).get("id"));
                        arrayList.remove(position);
                        arrayList = mDBHelper.showAll();
                        myAdapter.notifyItemRemoved(position);

                        break;

                }
            }
        });
        helper.attachToRecyclerView(recyclerView);
    }

}