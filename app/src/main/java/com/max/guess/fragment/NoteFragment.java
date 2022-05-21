package com.max.guess.fragment;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;


import static com.max.guess.util.Constants.KEY_NOTE;
import static com.max.guess.util.Constants.KEY_SQL_ACTION;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


import com.max.guess.R;
import com.max.guess.bean.Note;
import com.max.guess.util.SqlAction;

import java.sql.Timestamp;

public class NoteFragment extends Fragment implements DialogInterface.OnClickListener {
    private AppCompatActivity activity;
    private ActionBar actionBar;
    private EditText etNote, etNoteTitle;
    private Note note;
    private SqlAction sqlAction;
    // 定義並實例化OnBackPressedCallback，可在Fragment中監聽返回鍵
    private final OnBackPressedCallback callback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            etNoteTitle = new EditText(activity);
            if (sqlAction == SqlAction.INSERT) {
                new AlertDialog.Builder(activity)
                    .setView(etNoteTitle)
                    .setTitle(R.string.textSaveNote)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setMessage(R.string.textEnterTitle)
                    .setPositiveButton(R.string.textSave, NoteFragment.this)
                    .setNegativeButton(R.string.textCancel, NoteFragment.this)
                    .setCancelable(false)
                    .show();
            } else {
                sendNote();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (AppCompatActivity) getActivity();
        // 註冊返回鍵監聽器
        activity.getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        handleActionBar();
        return inflater.inflate(R.layout.fragment_note, container, false);
    }

    private void handleActionBar() {
        actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.textNote);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
    }

    private void findViews(View view) {
        etNote = view.findViewById(R.id.etContent);
    }

    @Override
    public void onStart() {
        super.onStart();
        handleEditText();
    }

    private void handleEditText() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            sqlAction = SqlAction.UPDATE;
            note = (Note) bundle.get(KEY_NOTE);
            if (actionBar != null) {
                actionBar.setSubtitle(note.getTitle());
            }
            etNote.setText(note.getContent());
        } else {
            sqlAction = SqlAction.INSERT;
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case BUTTON_POSITIVE:
                sendNote();
                break;
            case BUTTON_NEGATIVE:
                dialog.cancel();
                break;
        }
    }

    /**
     * 傳遞筆記資料至ListFragment
     */
    private void sendNote() {
        if (sqlAction == SqlAction.INSERT) {
            note = new Note();
        }
        final CharSequence title =
            sqlAction == SqlAction.INSERT ?
                etNoteTitle.getText() :
                actionBar.getSubtitle();
        note.setTitle(String.valueOf(title));
        note.setContent(String.valueOf(etNote.getText()));
        note.setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_SQL_ACTION, sqlAction);
        bundle.putSerializable(KEY_NOTE, note);
        NavController navController = Navigation.findNavController(etNote);
        navController.navigate(R.id.actionNoteToList, bundle);
    }
}