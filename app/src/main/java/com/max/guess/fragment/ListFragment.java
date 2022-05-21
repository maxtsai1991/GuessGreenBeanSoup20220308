package com.max.guess.fragment;



import static com.max.guess.util.Constants.KEY_THEME_MODE;
import static com.max.guess.util.Constants.PREFERENCES_FILE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.max.guess.R;
import com.max.guess.bean.Book;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {
    private AppCompatActivity activity;
    private SharedPreferences sharedPreferences;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = (AppCompatActivity) getActivity();
        sharedPreferences = activity.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        loadPreferences();
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        handleActionBar();
        handleRecyclerView();
    }

    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
    }

    /**
     * 偏好設定檔-讀檔
     */
    private void loadPreferences() {
        // 讀入資料
        final int themeMode = sharedPreferences.getInt(KEY_THEME_MODE, 1);
        AppCompatDelegate.setDefaultNightMode(themeMode);
    }

    /**
     * ActionBar相關處理
     */
    private void handleActionBar() {
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.textList);
        }
    }

    private void handleRecyclerView() {
        recyclerView.setAdapter(new MyAdapter(activity, getBookList()));
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
    }

    private static class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private final Context context;
        private final List<Book> list;

        public MyAdapter(Context context, List<Book> list) {
            this.context = context;
            this.list = list;
        }

        private static class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView textView;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
                textView = itemView.findViewById(R.id.textView);
            }
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_view, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            final Book book = list.get(position);
            holder.imageView.setImageResource(book.getImageId());
            holder.textView.setText(book.getTitle());
        }
    }

    /**
     * 取得書籍測試資料
     */
    private List<Book> getBookList() {
        List<Book> bookList = new ArrayList<>();
        bookList.add(new Book(R.drawable.book01, "突破困境：資安開源工具應用"));
        bookList.add(new Book(R.drawable.book02, "大話 AWS 雲端架構：雲端應用架構圖解輕鬆學"));
        bookList.add(new Book(R.drawable.book03, "為你自己學 Git"));
        bookList.add(new Book(R.drawable.book04, "無瑕的程式碼－整潔的軟體設計與架構篇"));
        bookList.add(new Book(R.drawable.book05, "大話設計模式"));
        bookList.add(new Book(R.drawable.book06, "無瑕的程式碼－敏捷軟體開發技巧守則"));
        bookList.add(new Book(R.drawable.book07, "iOS App 程式開發實務攻略：快速精通 SwiftUI"));
        bookList.add(new Book(R.drawable.book08, "獨角獸專案｜看IT部門如何引領百年企業振衰起敝，重返榮耀"));
        bookList.add(new Book(R.drawable.book09, "PowerShell 流程自動化攻略"));
        return bookList;
    }
}