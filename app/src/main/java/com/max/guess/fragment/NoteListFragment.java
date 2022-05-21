package com.max.guess.fragment;


import static com.max.guess.util.Constants.KEY_NOTE;
import static com.max.guess.util.Constants.KEY_SQL_ACTION;
import static com.max.guess.util.Constants.SDF_DATE;
import static com.max.guess.util.Constants.SDF_TIME;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.max.guess.R;
import com.max.guess.bean.Note;
import com.max.guess.dao.NoteDao;
import com.max.guess.util.SqlAction;

import java.util.List;

public class NoteListFragment extends Fragment {
    private AppCompatActivity activity;
    private RecyclerView recyclerView;
    private FloatingActionButton btAdd;
    private NoteDao noteDao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (AppCompatActivity) getActivity();
        // 2.1 實例化Dao物件
        noteDao = new NoteDao(activity);
        saveNote();
    }

    /**
     * 儲存筆記資料至SQLite
     * 可能是insert或update
     */
    private void saveNote() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            Note note = (Note) bundle.get(KEY_NOTE);
            SqlAction sqlAction = (SqlAction) bundle.get(KEY_SQL_ACTION);
            switch (sqlAction) {
                case INSERT:
                    noteDao.insert(note);
                    break;
                case UPDATE:
                    noteDao.update(note);
                    break;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        handleActionBar();
        return inflater.inflate(R.layout.fragment_note_list, container, false);
    }

    private void handleActionBar() {
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.textList);
            actionBar.setSubtitle("");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        handleRecyclerView();
        handleButton();
    }

    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        btAdd = view.findViewById(R.id.btAdd);
    }

    private void handleRecyclerView() {
        // 2.2 呼叫查詢方法
        List<Note> noteList = noteDao.selectAll();
        recyclerView.setAdapter(new NoteAdapter(activity, noteList));
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
    }

    private void handleButton() {
        btAdd.setOnClickListener(view -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.actionListToNote);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        NoteAdapter adapter = (NoteAdapter) recyclerView.getAdapter();
        // 2.2 呼叫查詢方法
        adapter.noteList = noteDao.selectAll();
        adapter.notifyDataSetChanged();
    }

    private static class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
        private final Context context;
        private List<Note> noteList;

        public NoteAdapter(Context context, List<Note> noteList) {
            this.context = context;
            this.noteList = noteList;
        }

        @Override
        public int getItemCount() {
            return noteList == null ? 0 : noteList.size();
        }

        private static class NoteViewHolder extends RecyclerView.ViewHolder {
            final TextView tvTitle, tvDate, tvTime;

            public NoteViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tvTitle);
                tvDate = itemView.findViewById(R.id.tvDate);
                tvTime = itemView.findViewById(R.id.tvTime);
            }
        }

        @NonNull
        @Override
        public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_view_sqlite, parent, false);
            return new NoteViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
            final Note note = noteList.get(position);
            holder.tvTitle.setText(note.getTitle());
            holder.tvDate.setText(SDF_DATE.format(note.getLastUpdateTime()));
            holder.tvTime.setText(SDF_TIME.format(note.getLastUpdateTime()));
            holder.itemView.setOnClickListener(view -> {
                NavController navController = Navigation.findNavController(view);
                Bundle bundle = new Bundle();
                bundle.putSerializable(KEY_NOTE, note);
                navController.navigate(R.id.actionListToNote, bundle);
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 2.3 關閉
        noteDao.close();
    }
}