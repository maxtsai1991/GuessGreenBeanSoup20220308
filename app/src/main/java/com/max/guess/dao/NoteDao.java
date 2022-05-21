package com.max.guess.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;


import com.max.guess.bean.Note;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 1. 自定義Dao類別
 * 1.1 繼承SQLiteOpenHelper
 */
public class NoteDao extends SQLiteOpenHelper {
    private static final String TAG = "TAG_NoteDao";
    // 1.2 欄位
    // 資料庫名
    private static final String DB_NAME = "MY_NOTE";
    // 資料庫版本號
    private static final int DB_VERSION = 1;
    // 資料表名
    private static final String TABLE_NAME = "NOTE";
    // 初始化SQL敘述
    private static final String INIT_SQL =
        "create table " + TABLE_NAME + "( " +
            "ID INTEGER primary key autoincrement," +
            "TITLE TEXT not null," +
            "CONTENT TEXT," +
            "LAST_UPDATE_TIME INTEGER not null)";
    // 各欄位名
    private static final String[] COLUMN_NAMES = {"ID", "TITLE", "CONTENT", "LAST_UPDATE_TIME"};

    /**
     * 1.3 建構子: 1個參數(Context型態)
     */
    public NoteDao(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * 1.4 覆寫方法
     * 1.4.1 onCreate()
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(INIT_SQL);
    }

    /**
     * 1.4.2 onUpgrade()
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * 1.5 定義方法: 定義新增方法
     */
    public long insert(Note note) {
        // 1.5.1 取得SQLiteDatabase物件
        SQLiteDatabase db = getWritableDatabase();
        // 1.5.2 實例化ContentValues物件，並放入欄位名及值
        ContentValues values = new ContentValues();
        values.put("TITLE", note.getTitle());
        values.put("CONTENT", note.getContent());
        values.put("LAST_UPDATE_TIME", note.getLastUpdateTime().getTime());
        // 1.5.3 執行SQL敘述
        return db.insert(TABLE_NAME, null, values);
    }

    /**
     * 1.5 定義方法: 定義刪除方法
     */
    public int delete(Integer id) {
        // 1.5.1 取得SQLiteDatabase物件
        SQLiteDatabase db = getWritableDatabase();
        // 1.5.3 執行SQL敘述
        return db.delete(
            TABLE_NAME,
            "ID = ?",
            new String[]{String.valueOf(id)}
        );
    }

    /**
     * 1.5 定義方法: 定義修改方法
     */
    public int update(Note note) {
        // 1.5.1 取得SQLiteDatabase物件
        SQLiteDatabase db = getWritableDatabase();
        // 1.5.2 實例化ContentValues物件，並放入欄位名及值
        ContentValues values = new ContentValues();
        values.put("TITLE", note.getTitle());
        values.put("CONTENT", note.getContent());
        values.put("LAST_UPDATE_TIME", note.getLastUpdateTime().getTime());
        // 1.5.3 執行SQL敘述
        return db.update(
            TABLE_NAME,
            values,
            "ID = ?",
            new String[]{String.valueOf(note.getId())}
        );
    }

    /**
     * 1.5 定義方法: 定義用ID(PK)查詢方法
     */
    public Note selectById(Integer id) {
        // 1.5.1 取得SQLiteDatabase物件
        SQLiteDatabase db = getReadableDatabase();
        try (
            // 1.5.3 執行SQL敘述
            Cursor cursor = db.query(
                TABLE_NAME,
                COLUMN_NAMES,
                "ID = ?",
                new String[]{String.valueOf(id)},
                null, null, null)
        ) {
            // 1.5.4 處理查詢結果
            if (cursor.moveToNext()) {
                Note note = new Note();
                note.setId(cursor.getInt(0));
                note.setTitle(cursor.getString(1));
                note.setContent(cursor.getString(2));
                note.setLastUpdateTime(new Timestamp(cursor.getLong(3)));
                return note;
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    /**
     * 1.5 定義方法: 定義查詢全部方法
     */
    public List<Note> selectAll() {
        // 1.5.1 取得SQLiteDatabase物件
        SQLiteDatabase db = getReadableDatabase();
        try (
            // 1.5.3 執行SQL敘述
            Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null)
        ) {
            List<Note> noteList = new ArrayList<>();
            // 1.5.4 處理查詢結果
            while (cursor.moveToNext()) {
                Note note = new Note();
                note.setId(cursor.getInt(0));
                note.setTitle(cursor.getString(1));
                note.setContent(cursor.getString(2));
                note.setLastUpdateTime(new Timestamp(cursor.getLong(3)));
                noteList.add(note);
            }
            return noteList;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }
}
