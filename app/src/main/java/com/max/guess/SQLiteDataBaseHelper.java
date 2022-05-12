package com.max.guess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 1. 繼承SQLiteOpenHelper
 */
public class SQLiteDataBaseHelper extends SQLiteOpenHelper {
    String TAG = SQLiteDataBaseHelper.class.getSimpleName();
    /**
     * 4. 建立建構子內的物件 String TableName
     */
    String TableName;

    /**
     * 3. 加入建構子
     */
    public SQLiteDataBaseHelper(@Nullable Context context, @Nullable String dataBaseName, @Nullable SQLiteDatabase.CursorFactory factory, int version, String TableName) {
        super(context, dataBaseName, factory, version);
        this.TableName = TableName;
    }
    /**
     * 2. 覆寫兩個方法 onCreate & onUpgrade
     * 4. 建立資料表 String SQLTable , 建立時請注意 , 因為是以字串輸入SQL語法 , 因此若有輸入錯誤不會提示 , 逗號要記得打 , 最後一個沒有逗號！！ , 欄位為TEXT的話就是一般字串格式
     *      SQLite 儲存類 (參考 : https://www.itread01.com/study/sqlite-data-types.html)
     *      儲存類	描述
     *      NULL	    值是一個 NULL 值。
     *      INTEGER	值是一個帶符號的整數，根據值的大小儲存在 1、2、3、4、6 或 8 位元組中。
     *      REAL	    值是一個浮點值，儲存為 8 位元組的 IEEE 浮點數字。
     *      TEXT	    值是一個文字字串，使用資料庫編碼（UTF-8、UTF-16BE 或 UTF-16LE）儲存。
     *      BLOB	    值是一個 blob 資料，完全根據它的輸入儲存。
     *      SQLite 的儲存類稍微比資料型別更普遍。INTEGER 儲存類，例如，包含 6 種不同的不同長度的整數資料型別。
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQLTable = "CREATE TABLE IF NOT EXISTS " + TableName + "( " +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Name TEXT, " +     // 姓名
                "Phone TEXT," +     // 電話
                "Hobby TEXT," +     // 興趣
                "ElseInfo TEXT" +   // 其他 //最後一個沒有逗號！！
                ");";
        db.execSQL(SQLTable);
    }

    /**
     * 5. 在onUpgrade的方法中填入
     *      onUpgrade方法 其他範例更詳細清楚 : https://mrraybox.blogspot.com/2017/01/android-sqlite-onupgrade.html
     *
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        final String SQL = "DROP TABLE " + TableName;
        db.execSQL(SQL);

    }

    /*=======================================自定義方法區↓=======================================*/

    /**
     * 6. 檢查資料表狀態，若無指定資料表則新增
     */
    public void chickTable(){
        Cursor cursor = getWritableDatabase().rawQuery(
                "select DISTINCT tbl_name from sqlite_master where tbl_name = '" + TableName + "'", null);
        if (cursor != null) {
            if (cursor.getCount() == 0)
                getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS " + TableName + "( " +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "Name TEXT, " +
                        "Phone TEXT," +
                        "Hobby TEXT," +
                        "ElseInfo TEXT" +
                        ");");
            cursor.close();
        }
    }

    /**
     * 7. 取得有多少資料表 , 並以陣列回傳
     */
    public ArrayList<String> getTables(){
        Cursor cursor = getWritableDatabase().rawQuery(
                "select DISTINCT tbl_name from sqlite_master", null);
        ArrayList<String> tables = new ArrayList<>();
        while (cursor.moveToNext()){
            String getTab = new String (cursor.getBlob(0));
            if (getTab.contains("android_metadata")){}
            else if (getTab.contains("sqlite_sequence")){}
            else tables.add(getTab.substring(0,getTab.length()-1));

        }
        return tables;
    }

    /**
     * 8. 新增資料
     * insert方法必須回傳(資料表名稱,nullColumnHack,值)
     * 那我們必須用ContentValues將所有值包起來，然後送給資料庫
     * 方法基礎就是HashMap的做法
     * 可參照橘色字部分
     * 另外種做法就是自己寫SQL語法
     * 而其中套用的就是
     * getWritableDatabase().execSQL();
     * 方法
     * 實際操作是這樣
     * SQLiteDatabase db = getWritableDatabase();
     * db.execSQL("");//這邊寫入SQL語法
     */
    public void addData(String name, String phone, String hobby, String elseInfo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Name", name);
        values.put("Phone", phone);
        values.put("Hobby", hobby);
        values.put("ElseInfo", elseInfo);
        db.insert(TableName, null, values);
    }

    /**
     * 9. 顯示所有資料
     */
    public ArrayList<HashMap<String, String>> showAll() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(" SELECT * FROM " + TableName, null);
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        while (c.moveToNext()) {
            HashMap<String, String> hashMap = new HashMap<>();

            // 從資料庫中撈資料
            String id = c.getString(0);
            String name = c.getString(1);
            String phone = c.getString(2);
            String hobby = c.getString(3);
            String elseInfo = c.getString(4);

            // 規定HashMap要回傳的陣列內容
            hashMap.put("id", id);
            hashMap.put("name", name);
            hashMap.put("phone", phone);
            hashMap.put("hobby", hobby);
            hashMap.put("elseInfo", elseInfo);
            arrayList.add(hashMap);
        }
        return arrayList;
    }

    /**
     * 10. 以id搜尋特定資料
     */
    public ArrayList<HashMap<String,String>> searchById(String getId){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(" SELECT * FROM " + TableName
                + " WHERE _id =" + "'" + getId + "'", null);
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        while (c.moveToNext()) {
            HashMap<String, String> hashMap = new HashMap<>();

            // 從資料庫中撈資料
            String id = c.getString(0);
            String name = c.getString(1);
            String phone = c.getString(2);
            String hobby = c.getString(3);
            String elseInfo = c.getString(4);

            // 規定HashMap要回傳的陣列內容
            hashMap.put("id", id);
            hashMap.put("name", name);
            hashMap.put("phone", phone);
            hashMap.put("hobby", hobby);
            hashMap.put("elseInfo", elseInfo);
            arrayList.add(hashMap);
        }
        return arrayList;
    }

    /**
     * 11. 以興趣篩選資料
     */
    public ArrayList<HashMap<String, String>> searchByHobby(String getHobby) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(" SELECT * FROM " + TableName
                + " WHERE Hobby =" + "'" + getHobby + "'", null);
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        while (c.moveToNext()) {
            HashMap<String, String> hashMap = new HashMap<>();

            String id = c.getString(0);
            String name = c.getString(1);
            String phone = c.getString(2);
            String hobby = c.getString(3);
            String elseInfo = c.getString(4);

            hashMap.put("id", id);
            hashMap.put("name", name);
            hashMap.put("phone", phone);
            hashMap.put("hobby", hobby);
            hashMap.put("elseInfo", elseInfo);
            arrayList.add(hashMap);
        }
        return arrayList;
    }

    /**
     * 12. 修改資料(麻煩)
     */
    public void modify(String id, String name, String phone, String hobby, String elseInfo) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(" UPDATE " + TableName
                + " SET Name=" + "'" + name + "',"
                + "Phone=" + "'" + phone + "',"
                + "Hobby=" + "'" + hobby + "',"
                + "ElseInfo=" + "'" + elseInfo + "'"
                + " WHERE _id=" + "'" + id + "'");
    }

    /**
     * 12. 修改資料(簡單)
     */
    public void modifyEZ(String id, String name, String phone, String hobby, String elseInfo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Name", name);
        values.put("Phone", phone);
        values.put("Hobby", hobby);
        values.put("ElseInfo", elseInfo);
        db.update(TableName, values, "_id = " + id, null);
    }

    /**
     * 13. 刪除全部資料
     */
    public void deleteAll(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM"+TableName);
    }

    /**
     * 13. 刪除全部資料(簡單)
     */
    public void deleteByIdEZ(String id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TableName,"_id = " + id,null);
    }

    /*=======================================自定義方法區↑=======================================*/

}